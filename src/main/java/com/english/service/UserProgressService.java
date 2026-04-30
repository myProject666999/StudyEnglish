package com.english.service;

import com.english.entity.UserProgress;

import java.util.Map;

public interface UserProgressService {

    UserProgress findByUserId(Long userId);

    void updateWordProgress(Long userId, boolean isLearned, boolean isMastered);

    void updateArticleProgress(Long userId);

    void updateListeningProgress(Long userId);

    void updateQuestionProgress(Long userId, boolean isCorrect);

    void updateStudyDays(Long userId);

    Map<String, Object> getStatistics(Long userId);

    void clearAllProgress(Long userId);
}
