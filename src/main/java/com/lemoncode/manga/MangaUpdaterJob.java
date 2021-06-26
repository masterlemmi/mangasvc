package com.lemoncode.manga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

import static java.lang.String.format;

@EnableScheduling
@Configuration
@Slf4j
public class MangaUpdaterJob {

    @Autowired
    MangaService mangaService;

    @Autowired
    MangaUpdaterAsyncService asyncService;

    @Scheduled(cron = "0 0 * * * *")   //every hour
    // @Scheduled(cron = "0 * * * * ?")  //ever minute
    public void scheduleMangaFetchUpdates() {
        LocalDateTime start = LocalDateTime.now();
        Future<Void> future = asyncService.fetchUpdates();

        while (true) {
            if (future.isDone()) {
                log.info("Job is Done");
                break;
            }
            log.info("Job not yet done, wait 5 secs");
            sleep(5000);
        }

        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);

        System.out.println(format("finished updating manga list, duration %d s", duration.getSeconds()));
    }

    private void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sleeping" + e.getMessage());
        }
    }
}
