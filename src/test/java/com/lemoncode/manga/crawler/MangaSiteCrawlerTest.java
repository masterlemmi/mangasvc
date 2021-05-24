package com.lemoncode.manga.crawler;

import com.lemoncode.util.UrlNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MangaSiteCrawlerTest {


    @Test

    public void findAllManganelo() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://manganelo.com/chapter/tang_yin_zai_yi_jie/chapter_1");

        assertTrue(crawler instanceof ManganeloCrawler);
        CrawledData data = crawler.findAll();
        assertEquals(data.getMainUrl(), "https://manganelo.com/manga/tang_yin_zai_yi_jie");
        assertEquals(data.getMangaTitle(), "Tang Yin Zai Yi Jie");

        CrawledData.Chapter next = data.getNextChapter();
        CrawledData.Chapter current = data.getCurrentChapter();
        CrawledData.Chapter last = data.getLastChapter();
        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);

        assertEquals("Chapter 1", current.getTitle());
        assertEquals("Chapter 2", next.getTitle());
        assertNotNull(last.getTitle());

        assertEquals("https://manganelo.com/chapter/tang_yin_zai_yi_jie/chapter_1", current.getUrl());
        assertEquals("https://manganelo.com/chapter/tang_yin_zai_yi_jie/chapter_2", next.getUrl());
        assertNotNull(last.getUrl());
    }


    @Test
    public void findAllManhuadex() {
        SiteCrawler crawler = SiteCrawlerFactory.get("https://manhuadex.com/manhua/yuan-zun/chapter-1");

        assertTrue(crawler instanceof ManhuadexCrawler);
        String title = crawler.findMangaTitle();
        String mainUrl = UrlNormalizer.normalize(crawler.findMainUrl());
        String chapterTitle = crawler.findChapterTitle();
        assertEquals("Yuan Zun", title);
        assertEquals("Chapter 1", chapterTitle);
        assertEquals("https://manhuadex.com/manhua/yuan-zun", mainUrl);

        CrawledData.Chapter next = crawler.findNextChapter();
        CrawledData.Chapter current = crawler.findCurrentChapter();
        CrawledData.Chapter last = crawler.findLastChapter();

        assertNotNull(next);
        assertNotNull(current);
        assertNotNull(last);


        assertEquals("Chapter 1.5", next.getTitle());
        assertEquals("https://manhuadex.com/manhua/yuan-zun/chapter-1-5", UrlNormalizer.normalize(next.getUrl()));

        assertEquals("Chapter 1", current.getTitle());
        assertEquals("https://manhuadex.com/manhua/yuan-zun/chapter-1", UrlNormalizer.normalize(current.getUrl()));

        assertNotNull(last.getTitle());
        assertNotNull(last.getUrl());

    }

    @Test
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


}