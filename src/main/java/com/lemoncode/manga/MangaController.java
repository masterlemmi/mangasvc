package com.lemoncode.manga;

import com.lemoncode.manga.request.CreateNewEntryRequest;
import com.lemoncode.manga.request.UpdateChapterRequest;
import com.lemoncode.manga.request.UpdateReadStatusRequest;
import com.lemoncode.validator.ChapterValueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/manga")
public class MangaController {
    private final static Logger LOGGER = Logger.getLogger(MangaController.class.getName());

    @Autowired
    private MangaService mangaService;

    @Autowired
    private MangaUpdaterAsyncService mangaUpdater;


    @GetMapping
    public List<Manga> findAll() {
        return this.mangaService.findAll();
    }

    @GetMapping("/ongoing")
    public List<Manga> findAllOngoing() {
        return this.mangaService.findAllOngoing();
    }

    @GetMapping("/ended")
    public List<Manga> findAllEnded() {
        return this.mangaService.findAllEnded();
    }


    @GetMapping("/new")
    public List<Manga> newReleases() {
        return this.mangaService.findNew();
    }

    @GetMapping("/sites")
    public List<SupportedSite> supportedSites() {
        MangaSite[] sites = MangaSite.values();
        List<SupportedSite> res = new ArrayList<>();

        for (MangaSite s : sites) {
            SupportedSite supportedSite = new SupportedSite();
            supportedSite.setSites(Arrays.asList(s.getUrl()));
            supportedSite.setName(s.name());
            res.add(supportedSite);
        }
        return res;
    }

    @PostMapping("/fetch/updates")
    public String checkForNewUpdates() {
        return this.mangaService.fetchUpdates();
    }

    @PostMapping("/fetch/updates-async")
    public String checkForNewUpdatesAsync() {
        this.mangaUpdater.fetchUpdates();
        return "Job Submitted";
    }

    @GetMapping("/fetch/updates-status")
    public MangaUpdateResult checkStatus() {
        return this.mangaUpdater.getUpdates();
    }

    @PutMapping("/chapter")
    public Manga updateChapter(@RequestBody UpdateChapterRequest body) {

        //TODO: why validation doesn't work
        if (!ChapterValueValidator.isValid(body.getChapter()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_MODIFIED, "Unacceptable: chapter");

        return this.mangaService.updateChapter(body);

    }

    @DeleteMapping("/{id}")
    public Manga deleteManga(@PathVariable("id") final String id) {
        return this.mangaService.delete(id);
    }


    @PutMapping("/done")
    public Manga markRead(@RequestBody UpdateReadStatusRequest body) {
        return this.mangaService.markRead(body);
    }

    @PutMapping("/mark-ended/{id}")
    public Manga markEnd(@PathVariable("id") final Long id) {
        return this.mangaService.markEnded(id);
    }


    @PostMapping
    public Manga addManga(@Valid @RequestBody CreateNewEntryRequest request) {

        //TODO: why validation doesn't work. also latest shouldn't be allowed
        if (!ChapterValueValidator.isValid(request.getChapter()))
            throw new ResponseStatusException(
                    HttpStatus.NOT_MODIFIED, "Unacceptable: chapter");


        Manga saved = this.mangaService.save(request);
        return saved;
    }

    //TODO: mark up to date, add, delete, edit


}
