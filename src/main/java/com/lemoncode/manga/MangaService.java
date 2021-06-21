package com.lemoncode.manga;


import com.lemoncode.manga.crawler.CrawledData;
import com.lemoncode.manga.crawler.SiteCrawler;
import com.lemoncode.manga.crawler.SiteCrawlerFactory;
import com.lemoncode.manga.request.CreateNewEntryRequest;
import com.lemoncode.manga.request.UpdateChapterRequest;
import com.lemoncode.manga.request.UpdateReadStatusRequest;
import com.lemoncode.util.NotFoundException;
import com.lemoncode.util.ObjectGenerator;
import com.lemoncode.util.PHTime;
import com.lemoncode.util.UrlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MangaService {
    private final static Logger LOGGER = Logger.getLogger(MangaService.class.getName());
    @Autowired
    MangaRepository repository;

    @Autowired
    private UrlParser parser;


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
        Manga manga = ObjectGenerator.generate(m.getChapter());

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
        LOGGER.info("Updating " + noUpdates.size() + " etnries");
        int newUpdates = 0;

        for (Manga manga : noUpdates) {

            try {
                SiteCrawler siteCrawler = SiteCrawlerFactory.get(manga.getLastChapterUrl());
                CrawledData.Chapter data = siteCrawler.findNextChapter();
                if (data != null) {
                    LOGGER.info(manga.getTitle() + " has next chapter: " + data.getUrl());
                    newUpdates++;
                    manga.setLastChapterUrl(data.getUrl());
                    manga.setLastChapter(data.getTitle());
                    manga.setDoneRead(false);
                    manga.setHasUpdate(true);
                }
                repository.save(manga);
            } catch (Exception e) {
                LOGGER.info("error checking updates for " + manga.getTitle() + ": " + e.getMessage());
            }
        }


        LOGGER.info("There are " + newUpdates + " new updates.");

        return "200";
    }


    public Manga markRead(UpdateReadStatusRequest body) {
        Manga manga = repository.findById(body.getId());

        if (manga == null) {
            throw new NotFoundException();
        }

        if (body.isDone()) {
            LOGGER.info("Setting " + manga.getTitle() + ":" + manga.getLastChapter() + " to DONE");

            SiteCrawler siteCrawler = SiteCrawlerFactory.get(manga.getLastChapterUrl());
            CrawledData.Chapter data = siteCrawler.findNextChapter();

            if (data != null) {
                LOGGER.info(manga.getTitle() + " has next chapter: " + data.getUrl());
                manga.setLastChapterUrl(data.getUrl());
                manga.setLastChapter(data.getTitle());
                manga.setDoneRead(false);
                manga.setHasUpdate(true);
            } else {
                LOGGER.info(manga.getTitle() + " has no next chapter");
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
