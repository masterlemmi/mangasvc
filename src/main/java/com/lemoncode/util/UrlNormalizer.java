package com.lemoncode.util;

import com.lemoncode.manga.Manga;

public class UrlNormalizer {

    public static String normalize(String url) {
        if (url != null) {

            String trimmed = url.trim();
            if (trimmed.matches(".*[/]+$")) {
                return trimmed.replaceAll("/+$", "");
            } else {
                return trimmed;
            }
        }
        return null;
    }

    public static Manga normalize(Manga manga) {
        if (manga == null) return null;
        manga.setUrl(normalize(manga.getUrl()));
        manga.setLastChapterUrl(normalize(manga.getLastChapterUrl()));
        return manga;
    }
}
