package com.lemoncode.notification;

import lombok.Data;

@Data
public class TokenSaveRequest {
    private String token;
    private boolean simple;
}
