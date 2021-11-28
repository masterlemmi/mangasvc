package com.lemoncode.manga;

import lombok.Getter;

@Getter
public class MangaUpdateException extends RuntimeException {
    private final Integer httpStatus;
    private final String message;

    public MangaUpdateException(Integer httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
