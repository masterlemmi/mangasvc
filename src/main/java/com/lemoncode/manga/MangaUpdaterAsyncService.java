package com.lemoncode.manga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Slf4j
public class MangaUpdaterAsyncService {

    @Autowired
    MangaService mangaService;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile MangaUpdateResult result;

    @Async
    public Future<Void> fetchUpdates() {
        if (!this.running.get()) { //not running
            this.running.set(true);
            result = new MangaUpdateResult();
            result.setRunning(true);
            result.setStart(LocalDateTime.now());
            result.setStatus("RUNNING");

            try {
                String svcMsg = mangaService.fetchUpdates();
                LocalDateTime end = LocalDateTime.now();
                result.setEnd(end);
                Duration duration = Duration.between(result.getStart(), end);
                result.setDuration(duration);
                result.setMessage(svcMsg);
                result.setStatus("DONE");
                result.setRunning(false);
                this.running.set(false);

            } catch (Exception e) {
                LocalDateTime end = LocalDateTime.now();
                result.setEnd(end);
                Duration duration = Duration.between(result.getStart(), end);
                result.setDuration(duration);
                result.setMessage(e.getMessage());
                result.setStatus("FAIL");
                result.setRunning(false);
                this.running.set(false);
            }
        } else {
            log.info("A new fetch udpates request received and ignored since job is already running");
        }

        return null;
    }


    public MangaUpdateResult getUpdates() {
        return this.result;
    }


}
