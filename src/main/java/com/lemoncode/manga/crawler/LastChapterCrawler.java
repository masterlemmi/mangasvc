package com.lemoncode.manga.crawler;

import org.jsoup.nodes.Document;

import java.io.IOException;

abstract class LastChapterCrawler implements SiteCrawler {

    final Document doc;
    final String chapterUrl;


    LastChapterCrawler(String lastChapter) {
        this.chapterUrl = lastChapter;
        if (!chapterUrl.toLowerCase().contains("chapter")) {
            throw new IllegalArgumentException("must pass a url chapter");
        }
        try {
            LOGGER.info("Crawling site: " + chapterUrl);
            this.doc = jsoupConnection(chapterUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error crawling site. Can't retrieve dteails");
        }
    }
}
