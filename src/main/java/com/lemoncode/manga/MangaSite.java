package com.lemoncode.manga;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MangaSite {
    MANGANELO("manganelo.com"), MANHUADEX("manhuadex.com"), POMANGA("pomanga.com");

    private String url;

    MangaSite(String url) {
        this.url = url;
    }

    public static MangaSite from(Manga manga) {
        return from(manga.getUrl());
    }

    public static MangaSite from(String u) {
        log.info("Manga from url " + u);
        for (MangaSite mangaSite : values()) {
            if (u.contains(mangaSite.url))
                return mangaSite;
        }
        throw new UnsupportedOperationException(u + " not known");
    }
}
