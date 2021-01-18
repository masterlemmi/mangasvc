package com.lemoncode.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class PHTime {

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("Asia/Manila"));
    }
}
