package com.lemoncode.manga;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MangaSite {
    MANGANELO("manganelo.com", "manganato.com"),
    MANHUADEX("manhuadex.com"),
    POMANGA("pomanga.com"),
    BEST_MANHUAS("bestmanhuas.com"),
    AQUA_MANGA("aquamanga.com"),
    MANGAHA("www.mangaha.org"),
    ONE_PUNCH("onepunch-manga.com")

    ;

    private final String[] url;

    MangaSite(String... url) {
        this.url = url;
    }

    public static MangaSite from(String u) {
        log.info("Manga from url " + u);
        for (MangaSite mangaSite : values()) {
            for (String ul : mangaSite.url){
                if (u.contains(ul)) return mangaSite;
            }
        }
        throw new UnsupportedOperationException(u + " not known");
    }

    public String[] getUrl() {
        return url;
    }
}
