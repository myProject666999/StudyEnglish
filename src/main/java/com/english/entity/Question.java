package com.english.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer questionType;
    private String question;
    private String options;
    private String answer;
    private String analysis;
    private Integer difficulty;
    private Long wordId;
    private Long articleId;
    private Long listeningId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
