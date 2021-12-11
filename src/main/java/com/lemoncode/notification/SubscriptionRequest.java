package com.lemoncode.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubscriptionRequest {
    private final String subscriber; //token
    private final String topic;
}
