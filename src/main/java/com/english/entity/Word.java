package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long levelId;
    private String word;
    private String phonetic;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private String audioUrl;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String levelName;
}
