package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
    private String contentCn;
    private String author;
    private String source;
    private Integer isDaily;
    private LocalDate dailyDate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
