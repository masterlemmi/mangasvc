package com.lemoncode.manga.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.logging.Logger;

public interface SiteCrawler {

    Logger LOGGER = Logger.getLogger(SiteCrawler.class.getName());

    CrawledData findAll();

    CrawledData.Chapter findNextChapter();

    CrawledData.Chapter findLastChapter();

    CrawledData.Chapter findCurrentChapter();

    String findChapterTitle();

    String findMangaTitle();

    String findMainUrl();


    default Connection jsoupConnection(String url) {
        String proxyServer = System.getenv("http_proxy");

        if (StringUtils.isEmpty(proxyServer)) {
            LOGGER.info("No Proxy setup for Jsoup");
            return Jsoup.connect(url);
        }

        String[] arr = proxyServer.substring(7).split(":");
        return Jsoup.connect(url).proxy(arr[0], Integer.parseInt(arr[1])).timeout(2500);
    }


}
