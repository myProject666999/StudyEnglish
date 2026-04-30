package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String author;
    private String description;
    private String coverUrl;
    private String category;
    private Integer difficulty;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
