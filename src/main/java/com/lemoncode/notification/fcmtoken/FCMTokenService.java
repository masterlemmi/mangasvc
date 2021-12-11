package com.lemoncode.notification.fcmtoken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMTokenService {
    private final FCMTokenRepository fcmTokenRepository;

    public FCMToken save(FCMToken fcmToken) {
        return fcmTokenRepository.save(fcmToken);
    }


    public int deleteByToken(String token) {
        return fcmTokenRepository.deleteByToken(token);
    }

    public List<FCMToken> findAll() {
        return fcmTokenRepository.findAll();
    }

    public FCMToken findByUsernameAndToken(String username, String fcmToken) {
        return fcmTokenRepository.findByUsernameAndToken(username, fcmToken);
    }
}
