package com.lemoncode.manga.crawler;

import com.lemoncode.manga.MangaUpdateException;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;

abstract class LastChapterCrawler implements SiteCrawler {

    final Document doc;
    final String chapterUrl;


    LastChapterCrawler(String lastChapter) {
        this.chapterUrl = lastChapter;
//        if (!chapterUrl.toLowerCase().contains("chapter")) {
//            throw new IllegalArgumentException("must pass a url chapter");
//        }
        try {
            LOGGER.info("Crawling site: " + chapterUrl);
            this.doc = jsoupConnection(chapterUrl).get();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new MangaUpdateException(500, "Unknown host: " + e.getMessage() );
        } catch (HttpStatusException e) {
            e.printStackTrace();
            throw new MangaUpdateException(e.getStatusCode(), e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error crawling site. Can't retrieve dteails " + e.getMessage());
        }
    }
}
