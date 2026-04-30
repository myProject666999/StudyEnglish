package com.english.service.impl;

import com.english.entity.UserProgress;
import com.english.mapper.StudyRecordMapper;
import com.english.mapper.UserProgressMapper;
import com.english.mapper.UserQuestionMapper;
import com.english.mapper.UserWordMapper;
import com.english.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserProgressServiceImpl implements UserProgressService {

    @Autowired
    private UserProgressMapper userProgressMapper;

    @Autowired
    private UserWordMapper userWordMapper;

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Autowired
    private StudyRecordMapper studyRecordMapper;

    @Override
    public UserProgress findByUserId(Long userId) {
        UserProgress progress = userProgressMapper.selectByUserId(userId);
        if (progress == null) {
            progress = new UserProgress();
            progress.setUserId(userId);
            progress.setTotalWords(0);
            progress.setLearnedWords(0);
            progress.setMasteredWords(0);
            progress.setTotalArticles(0);
            progress.setReadArticles(0);
            progress.setTotalListenings(0);
            progress.setCompletedListenings(0);
            progress.setTotalQuestions(0);
            progress.setCorrectQuestions(0);
            progress.setStudyDays(0);
            progress.setContinuousDays(0);
            progress.setLastStudyDate(LocalDate.now());
            userProgressMapper.insert(progress);
        }
        return progress;
    }

    @Override
    public void updateWordProgress(Long userId, boolean isLearned, boolean isMastered) {
        UserProgress progress = findByUserId(userId);
        if (progress == null) {
            return;
        }
        
        if (isLearned) {
            progress.setLearnedWords(userWordMapper.countLearnedWords(userId));
        }
        if (isMastered) {
            progress.setMasteredWords(userWordMapper.countMasteredWords(userId));
        }
        progress.setTotalWords(userWordMapper.countLearnedWords(userId) + userWordMapper.countNewWords(userId));
        
        userProgressMapper.update(progress);
        updateStudyDays(userId);
    }

    @Override
    public void updateArticleProgress(Long userId) {
        UserProgress progress = findByUserId(userId);
        if (progress != null) {
            progress.setReadArticles(progress.getReadArticles() + 1);
            userProgressMapper.update(progress);
            updateStudyDays(userId);
        }
    }

    @Override
    public void updateListeningProgress(Long userId) {
        UserProgress progress = findByUserId(userId);
        if (progress != null) {
            progress.setCompletedListenings(progress.getCompletedListenings() + 1);
            userProgressMapper.update(progress);
            updateStudyDays(userId);
        }
    }

    @Override
    public void updateQuestionProgress(Long userId, boolean isCorrect) {
        UserProgress progress = findByUserId(userId);
        if (progress != null) {
            progress.setTotalQuestions(progress.getTotalQuestions() + 1);
            if (isCorrect) {
                progress.setCorrectQuestions(progress.getCorrectQuestions() + 1);
            }
            userProgressMapper.update(progress);
            updateStudyDays(userId);
        }
    }

    @Override
    public void updateStudyDays(Long userId) {
        UserProgress progress = findByUserId(userId);
        if (progress == null) {
            return;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate lastStudyDate = progress.getLastStudyDate();
        
        if (lastStudyDate == null || !lastStudyDate.equals(today)) {
            if (lastStudyDate != null && lastStudyDate.plusDays(1).equals(today)) {
                progress.setContinuousDays(progress.getContinuousDays() + 1);
            } else if (lastStudyDate == null || !lastStudyDate.equals(today)) {
                progress.setContinuousDays(1);
            }
            progress.setStudyDays(progress.getStudyDays() + 1);
            progress.setLastStudyDate(today);
            userProgressMapper.update(progress);
        }
    }

    @Override
    public Map<String, Object> getStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();
        UserProgress progress = findByUserId(userId);
        
        if (progress != null) {
            statistics.put("totalWords", progress.getTotalWords());
            statistics.put("learnedWords", progress.getLearnedWords());
            statistics.put("masteredWords", progress.getMasteredWords());
            statistics.put("readArticles", progress.getReadArticles());
            statistics.put("completedListenings", progress.getCompletedListenings());
            statistics.put("totalQuestions", progress.getTotalQuestions());
            statistics.put("correctQuestions", progress.getCorrectQuestions());
            statistics.put("studyDays", progress.getStudyDays());
            statistics.put("continuousDays", progress.getContinuousDays());
            
            if (progress.getTotalQuestions() > 0) {
                double accuracy = (double) progress.getCorrectQuestions() / progress.getTotalQuestions() * 100;
                statistics.put("accuracy", String.format("%.2f", accuracy));
            } else {
                statistics.put("accuracy", "0.00");
            }
        }
        
        statistics.put("newWords", userWordMapper.countNewWords(userId));
        statistics.put("reviewWords", userWordMapper.countReviewWords(userId, java.time.LocalDateTime.now()));
        statistics.put("wrongQuestions", userQuestionMapper.countWrongBook(userId));
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearAllProgress(Long userId) {
        userWordMapper.deleteByUserId(userId);
        userQuestionMapper.deleteByUserId(userId);
        studyRecordMapper.deleteByUserId(userId);
        userProgressMapper.deleteByUserId(userId);
        
        UserProgress progress = new UserProgress();
        progress.setUserId(userId);
        progress.setTotalWords(0);
        progress.setLearnedWords(0);
        progress.setMasteredWords(0);
        progress.setTotalArticles(0);
        progress.setReadArticles(0);
        progress.setTotalListenings(0);
        progress.setCompletedListenings(0);
        progress.setTotalQuestions(0);
        progress.setCorrectQuestions(0);
        progress.setStudyDays(0);
        progress.setContinuousDays(0);
        progress.setLastStudyDate(LocalDate.now());
        userProgressMapper.insert(progress);
    }
}
