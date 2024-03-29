package com.lemoncode.manga.crawler;

import com.lemoncode.manga.MangaSite;
import org.apache.commons.lang3.NotImplementedException;

public class SiteCrawlerFactory {

    public static SiteCrawler get(String chapterUrl) {

        MangaSite mangaSite = MangaSite.from(chapterUrl);

        switch (mangaSite) {
            case MANGANELO:
                return new ManganeloCrawler(chapterUrl);
            case MANHUADEX:
                return new ManhuadexCrawler(chapterUrl);
            case POMANGA:
                return new PoMangaCrawler(chapterUrl);
            case BEST_MANHUAS:
                return new BestManhuasCrawler(chapterUrl);
            case AQUA_MANGA:
                return new AquaMangaCrawler(chapterUrl);
            case MANGAHA:
                return new MangahaCrawler(chapterUrl);
            case ONE_PUNCH:
                return new OnePunchCrawlerNew(chapterUrl);
            case MANGAACE:
                return new MangaAceCrawler(chapterUrl);
            default:
                throw new NotImplementedException(mangaSite + " is not acceptable");

        }
    }

}
