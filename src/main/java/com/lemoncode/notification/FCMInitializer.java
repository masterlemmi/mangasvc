package com.lemoncode.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service
public class FCMInitializer {

    @PostConstruct
    public void initialize() {
        System.out.println("----------------------");
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    //requires GOOGLE_APPLICATION_CREDENTIALS env variable
                    .setCredentials(GoogleCredentials.getApplicationDefault()).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IllegalStateException("Eror initializing notification service: " + e.getMessage());
        }
    }

}