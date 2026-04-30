package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long bookId;
    private String title;
    private String content;
    private String translation;
    private Integer difficulty;
    private Integer wordCount;
    private String audioUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String bookTitle;
}
