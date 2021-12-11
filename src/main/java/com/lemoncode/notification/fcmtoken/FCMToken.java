package com.lemoncode.notification.fcmtoken;

import com.lemoncode.util.LocalDateTimeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "FCM_TOKEN")
@NoArgsConstructor
public class FCMToken {

    public FCMToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;
    @Column(name = "LAST_USED")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastUsed;
    @Column(name = "SIMPLE_MESSAGE")
    private Boolean simpleMessageFlag;


}
