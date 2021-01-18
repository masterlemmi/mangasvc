package com.lemoncode.manga.crawler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CrawledData {
    private String mainUrl;
    private String mangaTitle;
    private Chapter currentChapter;
    private Chapter lastChapter;
    private Chapter nextChapter;

    private boolean hasNext() {
        return this.nextChapter != null;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Chapter {
        private String title;
        private String url;
    }
}