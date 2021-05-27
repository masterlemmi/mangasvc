package com.lemoncode.manga;


import java.util.Arrays;

public enum MangaSite {
    MANGANELO("manganelo.com"), MANHUADEX("manhuadex.com"), POMANGA("pomanga.com");

    private String url;

    MangaSite(String url) {
        this.url = url;
    }

    public static MangaSite from(Manga manga) {
        return from(manga.getUrl());
    }

    public static MangaSite from(String url) {
        return Arrays.stream(values()).filter(s -> url.contains(s.url)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException(url + " not known"));
    }
}
