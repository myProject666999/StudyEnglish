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
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
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

    public String getQuestionText() {
        return this.question;
    }

    public void setQuestionText(String questionText) {
        this.question = questionText;
    }

    public String getCorrectAnswer() {
        return this.answer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.answer = correctAnswer;
    }

    public String getExplanation() {
        return this.analysis;
    }

    public void setExplanation(String explanation) {
        this.analysis = explanation;
    }
}
