package com.lemoncode.manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.lang.String.format;

@EnableScheduling
@Configuration
public class MangaUpdaterJob {

    @Autowired
    MangaService mangaService;

    @Scheduled(cron = "0 0 7-23 * * * ")   //every hour from 7am to 11pm
//    @Scheduled(cron = "0 * * * * ?")  //ever minute
    public void scheduleMangaFetchUpdates() {
        LocalDateTime start = LocalDateTime.now();
        String result = mangaService.fetchUpdates();
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);

        System.out.println(format("finished updating manga list Result: %s, duration %d s", result, duration.getSeconds()));

    }
}
