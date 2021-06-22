package com.lemoncode.manga;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class MangaUpdateResult {

    private boolean isRunning;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private String status;
    private String message;

}
