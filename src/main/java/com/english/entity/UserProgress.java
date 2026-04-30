package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Integer totalWords;
    private Integer learnedWords;
    private Integer masteredWords;
    private Integer totalArticles;
    private Integer readArticles;
    private Integer totalListenings;
    private Integer completedListenings;
    private Integer totalQuestions;
    private Integer correctQuestions;
    private Integer studyDays;
    private Integer continuousDays;
    private LocalDate lastStudyDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
