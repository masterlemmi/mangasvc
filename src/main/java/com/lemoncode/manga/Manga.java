package com.lemoncode.manga;

import com.lemoncode.util.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Cacheable(false)
public class Manga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    @Column(name = "LAST_CHAPTER")
    private String lastChapter;
    @Column(name = "LAST_CHAPTER_URL")
    private String lastChapterUrl;
    @Column(name = "HAS_UPDATE")
    private boolean hasUpdate;
    @Column(name = "DONE_READ")
    private boolean doneRead;
    @Column(name = "ENDED")
    private boolean ended;
    @Column(name = "UPDATE_CODE")
    private Integer updateCode; //httpcode
    @Column(name = "UPDATE_ERROR")
    private String updateError;
    @Column(name = "LAST_UPDATE_DATE")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastUpdateDate;
    private Integer rating;

    public void updateLastChapter(String nextChapterUrl) {

    }
}
