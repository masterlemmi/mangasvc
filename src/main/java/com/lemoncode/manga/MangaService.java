package com.lemoncode.manga;


import com.lemoncode.manga.crawler.CrawledData;
import com.lemoncode.manga.crawler.SiteCrawler;
import com.lemoncode.manga.crawler.SiteCrawlerFactory;
import com.lemoncode.manga.request.CreateNewEntryRequest;
import com.lemoncode.manga.request.UpdateChapterRequest;
import com.lemoncode.manga.request.UpdateReadStatusRequest;
import com.lemoncode.notification.DirectNotification;
import com.lemoncode.notification.FCMService;
import com.lemoncode.notification.fcmtoken.FCMToken;
import com.lemoncode.notification.fcmtoken.FCMTokenService;
import com.lemoncode.util.MangaInstanceGenerator;
import com.lemoncode.util.NotFoundException;
import com.lemoncode.util.PHTime;
import com.lemoncode.util.UrlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MangaService {
    private final MangaRepository repository;
    private final UrlParser parser;
    private final FCMService fcmService;
    private final FCMTokenService fcmTokenService;

    public List<Manga> findAll() {
        List<Manga> allManga = repository.findAll();
        allManga.sort(Comparator.comparing(Manga::isHasUpdate).reversed()
                .thenComparing(Manga::getTitle));
        return allManga;
    }

    public List<Manga> findNew() {
        return new ArrayList<>();
    }

    public Manga save(@Valid CreateNewEntryRequest m) {
        Manga manga = MangaInstanceGenerator.generate(m.getChapter());

        Manga existing = repository.findByTitle(manga.getTitle());

        if (existing != null) {
            throw new IllegalArgumentException(manga.getTitle() + " already exists in system");
        }

        return this.repository.save(manga);
    }

    public Manga updateChapter(UpdateChapterRequest req) {
        Manga manga = repository.findById(req.getId());

        if (manga == null) {
            throw new NotFoundException();
        }

        String chapter = req.getChapter();
        boolean toLatest = chapter.equalsIgnoreCase("latest");

        if (toLatest) {
            SiteCrawler siteCrawler = SiteCrawlerFactory.get(manga.getLastChapterUrl());
            CrawledData.Chapter lastChapter = siteCrawler.findLastChapter();
            manga.setLastChapter(lastChapter.getTitle());
            manga.setLastChapterUrl(lastChapter.getUrl());
            manga.setHasUpdate(false);
            manga.setDoneRead(true);
            manga.setLastUpdateDate(PHTime.now());

        } else {
            SiteCrawler siteCrawler = SiteCrawlerFactory.get(chapter);

            String title = siteCrawler.findMangaTitle();

            if (!manga.getTitle().equalsIgnoreCase(title)) {
                throw new IllegalArgumentException(manga.getTitle() + " v " + title + "--> doesn't match");
            }

            CrawledData.Chapter currentChapter = siteCrawler.findCurrentChapter();
            manga.setLastChapter(currentChapter.getTitle());
            manga.setLastChapterUrl(currentChapter.getUrl());
            manga.setHasUpdate(true);
            manga.setDoneRead(false);
            manga.setLastUpdateDate(PHTime.now());
        }

        return repository.save(manga);

    }

    public String fetchUpdates() {
        List<Manga> noUpdates = repository.findNoUpdatesAndDoneReadAndNotEnded();
        log.info("Updating " + noUpdates.size() + " etnries");
        List<Manga> newUpdatedManga = new ArrayList<>();

        for (Manga manga : noUpdates) {

            try {
                SiteCrawler siteCrawler = SiteCrawlerFactory.get(manga.getLastChapterUrl());
                CrawledData.Chapter data = siteCrawler.findNextChapter();
                if (data != null) {
                    log.info(manga.getTitle() + " has next chapter: " + data.getUrl());
                    manga.setLastChapterUrl(data.getUrl());
                    manga.setLastChapter(data.getTitle());
                    manga.setDoneRead(false);
                    manga.setHasUpdate(true);
                    manga.setLastUpdateDate(PHTime.now());
                    manga.setUpdateCode(200);
                    manga.setUpdateError(null);
                    newUpdatedManga.add(manga);
                    repository.save(manga);
                }


            } catch (MangaUpdateException e) {
                log.info("error checking updates for " + manga.getTitle() + ": " + e.getMessage());
                e.printStackTrace();
                manga.setUpdateCode(e.getHttpStatus());
                manga.setUpdateError(e.getMessage());
                repository.save(manga);
            } catch (Exception e) {
                log.info("error checking updates for " + manga.getTitle() + ": " + e.getMessage());
                e.printStackTrace();
                manga.setUpdateError(e.getMessage());
                repository.save(manga);
            }
        }
        log.info("There are " + newUpdatedManga.size() + " new updates.");
        sendNotifications(newUpdatedManga);
        return "There are " + newUpdatedManga.size() + " new updates.";
    }

    private void sendNotifications(List<Manga> newUpdatedManga) {
        if (CollectionUtils.isEmpty(newUpdatedManga)) return;

        List<FCMToken> tokens = fcmTokenService.findAll();

        if (CollectionUtils.isEmpty(tokens)) return;

        Map<Boolean, List<FCMToken>> tokensPartition = tokens.stream().collect(
                Collectors.partitioningBy(FCMToken::getSimpleMessageFlag));
        for (FCMToken token : tokensPartition.get(true)) {
            DirectNotification notification = new DirectNotification();
            String msg = "There are " + newUpdatedManga.size() + " updates to tracked documents.";
            notification.setTitle("Document Tracking");
            notification.setMessage(msg);
            notification.setTarget(token.getToken());
            fcmService.sendNotificationToTarget(notification);
        }

        for (FCMToken token : tokensPartition.get(false)) {
            for (Manga manga : newUpdatedManga) {
                DirectNotification notification = new DirectNotification();
                notification.setTitle(manga.getTitle());
                String msg = "New: " + manga.getLastChapter();
                notification.setMessage(msg);
                notification.setTarget(token.getToken());
                fcmService.sendNotificationToTarget(notification);
            }
        }
    }


    public Manga markRead(UpdateReadStatusRequest body) {
        Manga manga = repository.findById(body.getId());

        if (manga == null) {
            throw new NotFoundException();
        }

        if (body.isDone()) {
            log.info("Setting " + manga.getTitle() + ":" + manga.getLastChapter() + " to DONE");

            SiteCrawler siteCrawler = SiteCrawlerFactory.get(manga.getLastChapterUrl());
            CrawledData.Chapter data = siteCrawler.findNextChapter();

            if (data != null) {
                log.info(manga.getTitle() + " has next chapter: " + data.getUrl());
                manga.setLastChapterUrl(data.getUrl());
                manga.setLastChapter(data.getTitle());
                manga.setDoneRead(false);
                manga.setHasUpdate(true);
            } else {
                log.info(manga.getTitle() + " has no next chapter");
                manga.setDoneRead(true);
                manga.setHasUpdate(false);
            }
            return repository.save(manga);
        } else {
            manga.setDoneRead(false);
            manga.setHasUpdate(true);
            return repository.save(manga);
        }
    }

    public Manga delete(String id) {
        //TODO:
        return null;
    }

    public Manga markEnded(Long id) {
        Manga manga = repository.findById(id);

        if (manga == null) {
            throw new NotFoundException();
        }

        manga.setEnded(true);
        return repository.save(manga);
    }

    public List<Manga> findAllOngoing() {
        return repository.findOngoing();

    }

    public List<Manga> findAllEnded() {
        return repository.findEnded();
    }
}
