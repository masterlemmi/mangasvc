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
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/manga")
public class MangaController {
    private final static Logger LOGGER = Logger.getLogger(MangaController.class.getName());

    @Autowired
    private MangaService mangaService;


    @GetMapping
    public List<Manga> findAll() {
        return this.mangaService.findAll();
    }

    @GetMapping("/new")
    public List<Manga> newReleases() {
        return this.mangaService.findNew();
    }

    @PostMapping("/fetch/updates")
    public String checkForNewUpdates() {
        return this.mangaService.fetchUpdates();
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

    @PutMapping("/end/{id}")
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
