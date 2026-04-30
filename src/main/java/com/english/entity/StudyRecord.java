package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StudyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Integer recordType;
    private Long typeId;
    private String content;
    private Integer isCorrect;
    private Integer studyTime;
    private LocalDateTime createTime;
}
