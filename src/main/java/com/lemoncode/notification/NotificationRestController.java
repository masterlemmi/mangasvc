package com.lemoncode.notification;

import com.lemoncode.notification.fcmtoken.FCMToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

//FOR TESTING ONLY
@RestController
@RequiredArgsConstructor
public class NotificationRestController {

    private final FCMService fcm;

    @PostMapping("/manga/subscribe")
    public FCMToken subscribe(@RequestBody FCMToken tokenSaveRequest, @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        tokenSaveRequest.setUsername(username);
        return fcm.saveToken(tokenSaveRequest);
    }


    @DeleteMapping("/manga/unsubscribe/{token}")
    public void unsubscribe(@PathParam("token") String token, @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        fcm.deleteToken(token, username);
    }

    @PutMapping("/manga/notification")
    public FCMToken setNotificationType(@RequestBody FCMToken tokenUpdate, @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        tokenUpdate.setUsername(username);
        return fcm.setNotificationType(tokenUpdate);
    }

//    @PostMapping("/notification")
//    public void sendTargetedNotification(@RequestBody DirectNotification notification, @AuthenticationPrincipal Jwt principal) {
//        String username = principal.getClaimAsString("preferred_username");
//        fcm.sendNotificationToTarget(notification, username);
//    }
//
//    @PostMapping("/topic/notification")
//    public void sendNotificationToTopic(@RequestBody TopicNotification notification) {
//        fcm.sendNotificationToTopic(notification);
//    }
//
//    @PostMapping("/topic/subscription")
//    public void subscribeToTopic(@RequestBody SubscriptionRequest subscription) throws FirebaseMessagingException {
//        fcm.subscribeToTopic(subscription);
//    }

}
