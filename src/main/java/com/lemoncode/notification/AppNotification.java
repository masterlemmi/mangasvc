package com.lemoncode.notification;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
abstract class AppNotification {
    private String title;
    private String message;

}
