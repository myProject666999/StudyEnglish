package com.english.service;

import com.english.entity.Question;
import com.english.entity.UserQuestion;

import java.util.List;

public interface QuestionService {

    Question findById(Long id);

    List<Question> findByType(Integer questionType);

    List<Question> findRandomQuestions(Integer questionType, int limit);

    List<Question> findList(String keyword, Integer questionType, Integer difficulty);

    boolean addQuestion(Question question);

    boolean updateQuestion(Question question);

    boolean deleteQuestion(Long id);

    UserQuestion submitAnswer(Long userId, Long questionId, String userAnswer);

    UserQuestion addToWrongBook(Long userId, Long questionId);

    UserQuestion removeFromWrongBook(Long userId, Long questionId);

    List<UserQuestion> getWrongBook(Long userId, int limit);

    List<UserQuestion> getUserHistory(Long userId, int limit);

    int countWrongBook(Long userId);

    int countUserHistory(Long userId);

    void clearWrongBook(Long userId);
}
