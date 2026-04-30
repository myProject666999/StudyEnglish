package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long questionId;
    private Integer isCorrect;
    private Integer isWrongBook;
    private String userAnswer;
    private Integer attemptCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Question question;
}
