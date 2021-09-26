package com.lemoncode.manga.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;

public class AquaMangaCrawler extends LastChapterCrawler {

    private String chapterTitle;
    private String mainTitle;
    private String mainUrl;
    private String nextChapterUrl;

    public AquaMangaCrawler(String lastChapter) {
        super(lastChapter);
    }

    @Override
    public CrawledData findAll() {
        scanBreadCrumb();


        CrawledData data = new CrawledData();
        data.setMangaTitle(this.mainTitle);
        data.setMainUrl(this.mainUrl);
        data.setCurrentChapter(new CrawledData.Chapter(this.chapterTitle, this.chapterUrl));
        data.setNextChapter(this.findNextChapter());
        data.setLastChapter(this.findLastChapter());

        return data;
    }

    @Override
    public CrawledData.Chapter findNextChapter() {
        if (this.nextChapterUrl == null) {
            Optional<String> url = findNextChapterUrl();
            if (url.isEmpty()) return null; //no new chapters
            else this.nextChapterUrl = url.get();
        }

        SiteCrawler siteCrawler = new AquaMangaCrawler(nextChapterUrl);
        String nextChapterTitle = siteCrawler.findChapterTitle();

        return new CrawledData.Chapter(nextChapterTitle, nextChapterUrl);
    }

    @Override
    public CrawledData.Chapter findLastChapter() {
        if (this.mainUrl == null) {
            scanBreadCrumb();
        }

        Document doc = null;
        try {
            LOGGER.info("Crawling main url: " + mainUrl);
            doc = jsoupConnection(mainUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error crawling site. Can't retrieve dteails");
        }

        Element lastChapterElement = doc.select("li.wp-manga-chapter > a").get(0);
        String title = lastChapterElement.text();
        String url = lastChapterElement.attr("href");
        System.out.println("Last Title: " + title);
        System.out.println("Last URL: " + url);

        return new CrawledData.Chapter(title, url);
    }

    public static void main(String[] args) {
        String mangaUrl = "https://pomanga.com/manga/read-spy-x-family-manga/chapter-1";
        AquaMangaCrawler s = new AquaMangaCrawler(mangaUrl);
        s.findLastChapter();
    }

    @Override
    public CrawledData.Chapter findCurrentChapter() {
        if (this.chapterTitle == null)
            scanBreadCrumb();
        return new CrawledData.Chapter(this.chapterTitle, this.chapterUrl);
    }

    @Override
    public String findChapterTitle() {
        if (chapterTitle == null) {
            scanBreadCrumb();
        }

        return this.chapterTitle;
    }

    @Override
    public String findMangaTitle() {
        if (this.mainTitle == null) {
            scanBreadCrumb();
        }

        return this.mainTitle;
    }


    @Override
    public String findMainUrl() {
        if (this.mainUrl == null) {
            scanBreadCrumb();
        }

        return this.mainUrl;
    }

    private void scanBreadCrumb() {
        Elements breadcrum = doc.select("ol.breadcrumb > li "); // a with href

        Element main = breadcrum.get(2).selectFirst("a[href]");
        this.mainTitle = main.text();
        this.mainUrl = main.attr("href");
        this.chapterTitle = breadcrum.get(3).text();

    }


    private Optional<String> findNextChapterUrl() {
        if (this.nextChapterUrl != null)
            return Optional.of(this.nextChapterUrl);

        return doc.select("a[href].next_page").stream()
                .findFirst()
                .map(el -> el.attr("href"));
    }

}
