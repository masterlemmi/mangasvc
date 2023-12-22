package com.lemoncode.manga.crawler;

import com.lemoncode.util.UrlNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MangaSiteCrawlerTest {


    @Test

    public void findAllManganelo() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://chapmanganato.to/manga-wj973392/chapter-615");

        assertTrue(crawler instanceof ManganeloCrawler);
        CrawledData data = crawler.findAll();
        assertEquals(data.getMainUrl(), "https://chapmanganato.to/manga-wj973392");
        assertEquals(data.getMangaTitle(), "Star Martial God Technique");

        CrawledData.Chapter next = data.getNextChapter();
        CrawledData.Chapter current = data.getCurrentChapter();
        CrawledData.Chapter last = data.getLastChapter();
        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);

        assertEquals("Chapter 615", current.getTitle());
        assertEquals("Chapter 616", next.getTitle());
        assertNotNull(last.getTitle());

        assertEquals("https://chapmanganato.to/manga-wj973392/chapter-615", current.getUrl());
        assertEquals("https://chapmanganato.to/manga-wj973392/chapter-616", next.getUrl());
        assertNotNull(last.getUrl());
    }


 //   @Test
    public void findAllManhuadex() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://manhuadex.com/manhua/yuan-zun/chapter-1");

        assertTrue(crawler instanceof ManhuadexCrawler);
        String title = crawler.findMangaTitle();
        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());
        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Yuan Zun", title);
        assertEquals("Chapter 1", chapterTitle);
        assertEquals("https://manhuadex.com/am/manhua/yuan-zun", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        CrawledData.Chapter current = crawler.findCurrentChapter();
        CrawledData.Chapter last = crawler.findLastChapter();

        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);


        assertEquals("Chapter 1.5", next.getTitle());
        assertEquals("https://manhuadex.com/am/manhua/yuan-zun/chapter-1-5", UrlNormalizer.normalize(next.getUrl()));

        assertEquals("Chapter 1", current.getTitle());
        assertEquals("https://manhuadex.com/manhua/yuan-zun/chapter-1", UrlNormalizer.normalize(current.getUrl()));

        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

  //  @Test //TODO: remove or replace
    public void findAllPoManga() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://pomanga.com/manga/read-spy-x-family-manga/chapter-1");

        assertTrue(crawler instanceof PoMangaCrawler);
        String title = crawler.findMangaTitle();
        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());
        String chapterTitle = crawler.findChapterTitle();
            assertEquals("Spy X Family", title);
        assertEquals("chapter 1", chapterTitle);
        assertEquals("https://pomanga.com/manga/read-spy-x-family-manga", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        CrawledData.Chapter current = crawler.findCurrentChapter();
        CrawledData.Chapter last = crawler.findLastChapter();

        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);


        assertEquals("chapter 2", next.getTitle());
        assertEquals("https://pomanga.com/manga/read-spy-x-family-manga/chapter-2", UrlNormalizer.normalize(next.getUrl()));

        assertEquals("chapter 1", current.getTitle());
        assertEquals("https://pomanga.com/manga/read-spy-x-family-manga/chapter-1", UrlNormalizer.normalize(current.getUrl()));

        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

   // @Test
    public void findAllBestManhuas() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://bestmanhuas.com/manga/the-origin/chapter-36");

        assertTrue(crawler instanceof BestManhuasCrawler);
        String title = crawler.findMangaTitle();

        assertEquals("The Origin", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Chapter 36", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://bestmanhuas.com/manga/the-origin", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        CrawledData.Chapter current = crawler.findCurrentChapter();
        CrawledData.Chapter last = crawler.findLastChapter();

        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);


        assertEquals("Chapter 37", next.getTitle());
        assertEquals("https://bestmanhuas.com/manga/the-origin/chapter-37", UrlNormalizer.normalize(next.getUrl()));

        assertEquals("Chapter 36", current.getTitle());
        assertEquals("https://bestmanhuas.com/manga/the-origin/chapter-36", UrlNormalizer.normalize(current.getUrl()));

        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

   // @Test
    public void findAquaManga() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://aquamanga.com/read/boss-in-school/chapter-74");

        assertTrue(crawler instanceof AquaMangaCrawler);
        String title = crawler.findMangaTitle();

        assertEquals("Boss in School", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Chapter 74", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://aquamanga.com/read/boss-in-school", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        assertNotNull(next);
        assertEquals("Chapter 75", next.getTitle());
        assertEquals("https://aquamanga.com/read/boss-in-school/chapter-75", UrlNormalizer.normalize(next.getUrl()));

        CrawledData.Chapter current = crawler.findCurrentChapter();
        assertNotNull(current);
        assertEquals("Chapter 74", current.getTitle());
        assertEquals("https://aquamanga.com/read/boss-in-school/chapter-74", UrlNormalizer.normalize(current.getUrl()));

        CrawledData.Chapter last = crawler.findLastChapter();
        assertNotNull(last);
        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }


  //  @Test
    public void findAMangaha() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://www.mangaha.org/manga/spy-x-family/chapter-55");

        assertTrue(crawler instanceof MangahaCrawler);
        String title = crawler.findMangaTitle();

        assertEquals("Spy X Family", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Chapter 55", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://www.mangaha.org/manga/spy-x-family", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        assertNotNull(next);
        assertEquals("Chapter 56", next.getTitle());
        assertEquals("https://www.mangaha.org/manga/spy-x-family/chapter-56", UrlNormalizer.normalize(next.getUrl()));

        CrawledData.Chapter current = crawler.findCurrentChapter();
        assertNotNull(current);
        assertEquals("Chapter 55", current.getTitle());
        assertEquals("https://www.mangaha.org/manga/spy-x-family/chapter-55", UrlNormalizer.normalize(current.getUrl()));

        CrawledData.Chapter last = crawler.findLastChapter();
        assertNotNull(last);
        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }
   // @Test
    public void findOnePunchOld() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://onepunch-manga.com/manga/one-punch-man-ch-142/");

        assertTrue(crawler instanceof OnePunchCrawler);
        String title = crawler.findMangaTitle();

        assertEquals("One Punch Man", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Chapter 142", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://onepunch-manga.com", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        assertNotNull(next);
        assertEquals("Chapter 143", next.getTitle());
        assertEquals("https://onepunch-manga.com/manga/one-punch-man-chapters-readmangaa-143", UrlNormalizer.normalize(next.getUrl()));

        CrawledData.Chapter current = crawler.findCurrentChapter();
        assertNotNull(current);
        assertEquals("Chapter 142", current.getTitle());
        assertEquals("https://onepunch-manga.com/manga/one-punch-man-ch-142", UrlNormalizer.normalize(current.getUrl()));

        CrawledData.Chapter last = crawler.findLastChapter();
        assertNotNull(last);
        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

   // @Test
    public void findOnePunch() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://onepunchmansmanga.com/manga/one-punch-man-chapter-173/");

        assertTrue(crawler instanceof OnePunchCrawlerNew);
        String title = crawler.findMangaTitle();

        assertEquals("One punch man", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("chapter 173", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://onepunchmansmanga.com", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        assertNotNull(next);
        assertEquals("chapter 174", next.getTitle());
        assertEquals("https://onepunchmansmanga.com/manga/one-punch-man-chapter-174", UrlNormalizer.normalize(next.getUrl()));

        CrawledData.Chapter current = crawler.findCurrentChapter();
        assertNotNull(current);
        assertEquals("chapter 173", current.getTitle());
        assertEquals("https://onepunchmansmanga.com/manga/one-punch-man-chapter-173", UrlNormalizer.normalize(current.getUrl()));

        CrawledData.Chapter last = crawler.findLastChapter();
        assertNotNull(last);
        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

    @Test
    public void findManagaAce() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://mangace.com/chapter/spy-x-family-chapter-70/");

        assertTrue(crawler instanceof MangaAceCrawler);
        String title = crawler.findMangaTitle();

        assertEquals("SPY×FAMILY", title);

        String chapterTitle = crawler.findChapterTitle();
        assertEquals("spy x family chapter 70", chapterTitle);

        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());

        assertEquals("https://mangace.com/manga/spy-x-family", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        assertNotNull(next);
        assertEquals("spy x family chapter 71", next.getTitle());
        assertEquals("https://mangace.com/chapter/spy-x-family-chapter-71", UrlNormalizer.normalize(next.getUrl()));

        CrawledData.Chapter current = crawler.findCurrentChapter();
        assertNotNull(current);
        assertEquals("spy x family chapter 70", current.getTitle());
        assertEquals("https://mangace.com/chapter/spy-x-family-chapter-70", UrlNormalizer.normalize(current.getUrl()));

        CrawledData.Chapter last = crawler.findLastChapter();
        assertNotNull(last);
        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }



}