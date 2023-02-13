package com.lemoncode.manga.crawler;

import com.lemoncode.manga.MangaSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;

public class MangaAceCrawler extends LastChapterCrawler {

    private String chapterTitle;
    private String mainTitle;
    private String mainUrl;
    private String nextChapterUrl;
    private final static String BASE_SITE = "https://" + MangaSite.ONE_PUNCH.getUrl()[0];

    public MangaAceCrawler(String lastChapter) {
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

        SiteCrawler siteCrawler = new MangaAceCrawler(nextChapterUrl);
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

        Elements lastChapterElement = doc.select("ul.chapterslist").get(0).children().get(0).select("li > a");
        String title = lastChapterElement.text();
        String url = this.mainUrl + lastChapterElement.attr("href");
        System.out.println("Last Title: " + title);
        System.out.println("Last URL: " + url);

        return new CrawledData.Chapter(title, url);
    }

    public static void main(String[] args) {
        String mangaUrl = "https://pomanga.com/manga/read-spy-x-family-manga/chapter-1";
        MangaAceCrawler s = new MangaAceCrawler(mangaUrl);
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
        Elements title = doc.select("h1.entry-title"); // a with href

        String header = title.get(0).text();
        this.chapterTitle = header.substring(header.lastIndexOf(",") + 1).trim();

        Element mainLink =  doc.select("h3.latest-title").select("a[href]").get(0);
        this.mainUrl = mainLink.attr("href");
        this.mainTitle = mainLink.text();

    }


    private Optional<String> findNextChapterUrl() {
        if (this.nextChapterUrl != null)
            return Optional.of(this.nextChapterUrl);

        return doc.select("div.next-post > a").stream()
                .findFirst()
                .map(el -> el.attr("href"));
    }

}
