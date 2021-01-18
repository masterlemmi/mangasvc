/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lemoncode.manga.crawler;

import com.lemoncode.util.UrlParser;

import com.lemoncode.util.NotFoundException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;


public class ManganeloCrawler extends LastChapterCrawler {

    private final static Logger LOGGER = Logger.getLogger(ManganeloCrawler.class.getName());

    private final UrlParser parser = new UrlParser();

    private String mainUrl;
    private String nextChapterUrl;

    ManganeloCrawler(String chapterUrl) {
        super(chapterUrl);

    }

    @Override
    public CrawledData.Chapter findLastChapter() {

        this.mainUrl = findMainUrl();

        Document doc = null;
        try {
            LOGGER.info("Crawling main url: " + mainUrl);
            doc = jsoupConnection(mainUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error crawling site. Can't retrieve dteails");
        }

        Elements links = doc.select("a.chapter-name").select("a[href]"); // a with href
        return links.stream()
                .map(el -> new CrawledData.Chapter(el.attr("title"), el.attr("href")))
                .findFirst()
                .orElseThrow(()-> new NotFoundException("not found"));
    }

    @Override
    public CrawledData.Chapter findCurrentChapter() {
        return new CrawledData.Chapter(findChapterTitle(), this.chapterUrl);
    }

    @Override
    public CrawledData.Chapter findNextChapter() {
        if (this.nextChapterUrl == null) {
            Optional<String> url = findNextChapterUrl();
            if (url.isEmpty()) return null;
            else this.nextChapterUrl = url.get();
        }

        SiteCrawler siteCrawler = new ManganeloCrawler(nextChapterUrl);
        String nextChapterTitle = siteCrawler.findChapterTitle();

        return new CrawledData.Chapter(nextChapterTitle, nextChapterUrl);
    }


    Optional<String> findNextChapterUrl() {
        if (this.nextChapterUrl != null)
            return Optional.of(this.nextChapterUrl);


        Elements links = doc.select("a[href]"); // a with href
        return links.stream()
                .filter(el -> el.text().contains("NEXT CHAPTER"))
                .map(el -> el.attr("href"))
                .findFirst();

    }


    @Override
    public String findChapterTitle() {

        Elements links = doc.select("a.a-h").select("a[href]"); // a with href
        return links.stream().filter(l -> l.attr("href").contains(chapterUrl.trim()))
                .map(l -> {
                    String title = l.attr("title");
                    LOGGER.info(title);
                    return title;
                })
                .findFirst().orElse(parser.getLastString(chapterUrl));
    }

    @Override
    public String findMangaTitle() {
        Elements links = doc.select("a.a-h").select("a[href]"); // a with href

        boolean getNext = false;
        String title = "_";
        for (Element el : links) {
            if (getNext) {
                title = el.attr("title");
                break;
            }

            if (el.attr("title").equalsIgnoreCase("Read Manga Online"))
                getNext = true;
        }

        return title;
    }

    @Override
    public String findMainUrl() {
        if (this.mainUrl != null)
            return this.mainUrl;

        Elements links = doc.select("a.a-h").select("a[href]"); // a with href

        boolean getNext = false;
        String url = "_";
        for (Element el : links) {
            if (getNext) {
                url = el.attr("href");
                break;
            }

            if (el.attr("title").equalsIgnoreCase("Read Manga Online"))
                getNext = true;
        }

        return url;
    }

    @Override
    public CrawledData findAll() {
        Elements links = doc.select("a.a-h").select("a[href]"); // a with href

        boolean getNext = false;
        String mainUrl = null;
        String mangaTitle = "_";

        for (Element el : links) {
            if (getNext) {
                mangaTitle = el.attr("title");
                mainUrl = el.attr("href");
                break;
            }

            if (el.attr("title").equalsIgnoreCase("Read Manga Online"))
                getNext = true;
        }

        CrawledData data = new CrawledData();
        data.setMangaTitle(mangaTitle);
        data.setMainUrl(mainUrl);
        data.setNextChapter(this.findNextChapter());
        data.setCurrentChapter(this.findCurrentChapter());
        data.setLastChapter(this.findLastChapter());

        return data;
    }


}
