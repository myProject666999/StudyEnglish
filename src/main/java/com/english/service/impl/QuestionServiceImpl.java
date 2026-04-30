package com.english.service.impl;

import com.english.entity.Question;
import com.english.entity.UserQuestion;
import com.english.mapper.QuestionMapper;
import com.english.mapper.UserQuestionMapper;
import com.english.service.QuestionService;
import com.english.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Autowired
    private UserProgressService userProgressService;

    @Override
    public Question findById(Long id) {
        return questionMapper.selectById(id);
    }

    @Override
    public List<Question> findByType(Integer questionType) {
        return questionMapper.selectByType(questionType);
    }

    @Override
    public List<Question> findRandomQuestions(Integer questionType, int limit) {
        return questionMapper.selectRandom(questionType, limit);
    }

    @Override
    public List<Question> findList(String keyword, Integer questionType, Integer difficulty) {
        return questionMapper.selectList(keyword, questionType, difficulty);
    }

    @Override
    public boolean addQuestion(Question question) {
        return questionMapper.insert(question) > 0;
    }

    @Override
    public boolean updateQuestion(Question question) {
        return questionMapper.update(question) > 0;
    }

    @Override
    public boolean deleteQuestion(Long id) {
        return questionMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserQuestion submitAnswer(Long userId, Long questionId, String userAnswer) {
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            return null;
        }
        
        boolean isCorrect = question.getAnswer().equals(userAnswer);
        
        UserQuestion userQuestion = userQuestionMapper.selectByUserAndQuestion(userId, questionId);
        
        if (userQuestion == null) {
            userQuestion = new UserQuestion();
            userQuestion.setUserId(userId);
            userQuestion.setQuestionId(questionId);
            userQuestion.setIsCorrect(isCorrect ? 1 : 0);
            userQuestion.setIsWrongBook(isCorrect ? 0 : 1);
            userQuestion.setUserAnswer(userAnswer);
            userQuestion.setAttemptCount(1);
            userQuestionMapper.insert(userQuestion);
        } else {
            userQuestion.setIsCorrect(isCorrect ? 1 : 0);
            userQuestion.setUserAnswer(userAnswer);
            userQuestion.setAttemptCount(userQuestion.getAttemptCount() + 1);
            if (!isCorrect) {
                userQuestion.setIsWrongBook(1);
            }
            userQuestionMapper.update(userQuestion);
        }
        
        userProgressService.updateQuestionProgress(userId, isCorrect);
        
        return userQuestion;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserQuestion addToWrongBook(Long userId, Long questionId) {
        UserQuestion userQuestion = userQuestionMapper.selectByUserAndQuestion(userId, questionId);
        
        if (userQuestion == null) {
            userQuestion = new UserQuestion();
            userQuestion.setUserId(userId);
            userQuestion.setQuestionId(questionId);
            userQuestion.setIsWrongBook(1);
            userQuestion.setAttemptCount(0);
            userQuestionMapper.insert(userQuestion);
        } else {
            userQuestion.setIsWrongBook(1);
            userQuestionMapper.update(userQuestion);
        }
        
        return userQuestion;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserQuestion removeFromWrongBook(Long userId, Long questionId) {
        UserQuestion userQuestion = userQuestionMapper.selectByUserAndQuestion(userId, questionId);
        
        if (userQuestion != null) {
            userQuestion.setIsWrongBook(0);
            userQuestionMapper.update(userQuestion);
        }
        
        return userQuestion;
    }

    @Override
    public List<UserQuestion> getWrongBook(Long userId, int limit) {
        return userQuestionMapper.selectWrongBook(userId, limit);
    }

    @Override
    public List<UserQuestion> getUserHistory(Long userId, int limit) {
        return userQuestionMapper.selectByUserId(userId, limit);
    }

    @Override
    public int countWrongBook(Long userId) {
        return userQuestionMapper.countWrongBook(userId);
    }

    @Override
    public int countUserHistory(Long userId) {
        return userQuestionMapper.countByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearWrongBook(Long userId) {
        userQuestionMapper.deleteWrongBook(userId);
    }
}
