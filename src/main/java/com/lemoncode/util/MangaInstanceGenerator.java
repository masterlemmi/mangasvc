package com.lemoncode.util;

import com.lemoncode.manga.Manga;
import com.lemoncode.manga.crawler.SiteCrawlerFactory;
import com.lemoncode.manga.crawler.SiteCrawler;

public class MangaInstanceGenerator {


    public static Manga generate(String chapterUrl) {
        SiteCrawler crawler = SiteCrawlerFactory.get(chapterUrl);

        Manga manga = new Manga();
        manga.setTitle(crawler.findMangaTitle());
        manga.setLastUpdateDate(PHTime.now());
        manga.setDoneRead(false);
        manga.setHasUpdate(true);
        manga.setLastChapterUrl(chapterUrl);
        manga.setLastChapter(crawler.findChapterTitle());
        manga.setUrl(crawler.findMainUrl());
        manga.setEnded(false);


        return manga;
    }

}
