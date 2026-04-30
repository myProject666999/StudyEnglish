package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserWord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long wordId;
    private Integer isNew;
    private Integer isLearned;
    private Integer isMastered;
    private Integer reviewCount;
    private LocalDateTime lastReviewTime;
    private LocalDateTime nextReviewTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Word word;
}
