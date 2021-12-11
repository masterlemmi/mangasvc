package com.lemoncode.notification;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import com.lemoncode.notification.fcmtoken.FCMToken;
import com.lemoncode.notification.fcmtoken.FCMTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    private final FCMTokenService fcmTokenService;

    public String sendNotificationToTarget(DirectNotification notification) {
        try {
            val message = Message.builder()
                    // Set the configuration for our web notification
                    .setWebpushConfig(
                            // Create and pass a WebpushConfig object setting the notification
                            WebpushConfig.builder()
                                    .setNotification(
                                            // Create and pass a web notification object with the specified title, body, and icon URL
                                            WebpushNotification.builder()
                                                    .setTitle(notification.getTitle())
                                                    .setBody(notification.getMessage())
                                                    .setIcon("https://drive.google.com/uc?export=view&amp;id=1oOJOJ42Iese4NFs4FkRUZSiewYuFQThz")
                                                    .build()
                                    ).build()
                    )
                    // Specify the user to send it to in the form of their token
                    .setToken(notification.getTarget())
                    .build();
            String result = FirebaseMessaging.getInstance().send(message);
            return result;
        } catch (FirebaseMessagingException fme) {
            fcmTokenService.deleteByToken(notification.getTarget());
            log.error("Firebase token verification exception", fme);

        }

        return null;
    }

    public ApiFuture<String> sendNotificationToTopic(TopicNotification notification) {
        val message = Message.builder()
                .setWebpushConfig(
                        WebpushConfig.builder()
                                .setNotification(
                                        WebpushNotification.builder()
                                                .setTitle(notification.getTitle())
                                                .setBody(notification.getMessage())
                                                .setIcon("https://drive.google.com/uc?export=view&amp;id=1oOJOJ42Iese4NFs4FkRUZSiewYuFQThz")
                                                .build()
                                ).build()
                ).setTopic(notification.getTopic())
                .build();

        return FirebaseMessaging.getInstance().sendAsync(message);
    }


    public TopicManagementResponse subscribeToTopic(SubscriptionRequest subscription) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().subscribeToTopic(Collections.singletonList(subscription.getSubscriber()), subscription.getTopic());
    }

    public FCMToken saveToken(FCMToken token) {
        FCMToken fcmToken = fcmTokenService.findByUsernameAndToken(token.getUsername(), token.getToken());
        if (fcmToken == null) {
            fcmToken = token;
            fcmToken.setUsername(token.getUsername());
            fcmToken.setSimpleMessageFlag(true);
        }
        fcmToken.setLastUsed(LocalDateTime.now());
        return this.fcmTokenService.save(fcmToken);
    }

    public FCMToken setNotificationType(FCMToken token) {
        FCMToken fcmToken = fcmTokenService.findByUsernameAndToken(token.getUsername(), token.getToken());
        if (fcmToken == null) {
            log.info("FCM TOKEN being update does not exist in db");
            return null;
        }
        fcmToken.setSimpleMessageFlag(token.getSimpleMessageFlag());
        return this.fcmTokenService.save(fcmToken);
    }

    public void deleteToken(String token, String username) {
        FCMToken fcmToken = fcmTokenService.findByUsernameAndToken(username, token);
        if (fcmToken == null) return;
        this.fcmTokenService.deleteByToken(token);
    }
}
