package com.english.service.impl;

import com.english.entity.Word;
import com.english.entity.WordLevel;
import com.english.entity.UserWord;
import com.english.mapper.WordMapper;
import com.english.mapper.WordLevelMapper;
import com.english.mapper.UserWordMapper;
import com.english.service.WordService;
import com.english.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordLevelMapper wordLevelMapper;

    @Autowired
    private UserWordMapper userWordMapper;

    @Autowired
    private UserProgressService userProgressService;

    @Override
    public Word findById(Long id) {
        return wordMapper.selectById(id);
    }

    @Override
    public WordLevel findLevelById(Long id) {
        return wordLevelMapper.selectById(id);
    }

    @Override
    public List<WordLevel> findAllLevels() {
        return wordLevelMapper.selectAll();
    }

    @Override
    public List<Word> findByLevelId(Long levelId) {
        return wordMapper.selectByLevelId(levelId);
    }

    @Override
    public List<Word> findRandomWords(Long levelId, int limit) {
        return wordMapper.selectRandom(levelId, limit);
    }

    @Override
    public List<Word> findList(String keyword, Long levelId) {
        return wordMapper.selectList(keyword, levelId);
    }

    @Override
    public boolean addWord(Word word) {
        return wordMapper.insert(word) > 0;
    }

    @Override
    public boolean updateWord(Word word) {
        return wordMapper.update(word) > 0;
    }

    @Override
    public boolean deleteWord(Long id) {
        return wordMapper.deleteById(id) > 0;
    }

    @Override
    public boolean addLevel(WordLevel wordLevel) {
        return wordLevelMapper.insert(wordLevel) > 0;
    }

    @Override
    public boolean updateLevel(WordLevel wordLevel) {
        return wordLevelMapper.update(wordLevel) > 0;
    }

    @Override
    public boolean deleteLevel(Long id) {
        return wordLevelMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord startLearning(Long userId, Long wordId) {
        UserWord userWord = userWordMapper.selectByUserAndWord(userId, wordId);
        
        if (userWord == null) {
            userWord = new UserWord();
            userWord.setUserId(userId);
            userWord.setWordId(wordId);
            userWord.setIsNew(1);
            userWord.setIsLearned(0);
            userWord.setIsMastered(0);
            userWord.setReviewCount(0);
            userWordMapper.insert(userWord);
            userProgressService.updateWordProgress(userId, false, false);
        }
        
        return userWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord markAsLearned(Long userId, Long wordId) {
        UserWord userWord = userWordMapper.selectByUserAndWord(userId, wordId);
        
        if (userWord == null) {
            userWord = new UserWord();
            userWord.setUserId(userId);
            userWord.setWordId(wordId);
            userWord.setIsNew(0);
            userWord.setIsLearned(1);
            userWord.setIsMastered(0);
            userWord.setReviewCount(0);
            userWord.setLastReviewTime(LocalDateTime.now());
            userWord.setNextReviewTime(LocalDateTime.now().plusDays(1));
            userWordMapper.insert(userWord);
        } else {
            userWord.setIsNew(0);
            userWord.setIsLearned(1);
            userWord.setLastReviewTime(LocalDateTime.now());
            userWord.setNextReviewTime(LocalDateTime.now().plusDays(1));
            userWordMapper.update(userWord);
        }
        
        userProgressService.updateWordProgress(userId, true, false);
        return userWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord markAsMastered(Long userId, Long wordId) {
        UserWord userWord = userWordMapper.selectByUserAndWord(userId, wordId);
        
        if (userWord != null) {
            userWord.setIsMastered(1);
            userWord.setLastReviewTime(LocalDateTime.now());
            userWordMapper.update(userWord);
            userProgressService.updateWordProgress(userId, true, true);
        }
        
        return userWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord addToNewWords(Long userId, Long wordId) {
        UserWord userWord = userWordMapper.selectByUserAndWord(userId, wordId);
        
        if (userWord == null) {
            userWord = new UserWord();
            userWord.setUserId(userId);
            userWord.setWordId(wordId);
            userWord.setIsNew(1);
            userWord.setIsLearned(0);
            userWord.setIsMastered(0);
            userWord.setReviewCount(0);
            userWordMapper.insert(userWord);
        } else if (userWord.getIsNew() != 1) {
            userWord.setIsNew(1);
            userWordMapper.update(userWord);
        }
        
        return userWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord reviewWord(Long userId, Long wordId, boolean isCorrect) {
        UserWord userWord = userWordMapper.selectByUserAndWord(userId, wordId);
        
        if (userWord != null) {
            userWord.setReviewCount(userWord.getReviewCount() + 1);
            userWord.setLastReviewTime(LocalDateTime.now());
            
            if (isCorrect) {
                int reviewCount = userWord.getReviewCount();
                if (reviewCount >= 5) {
                    userWord.setIsMastered(1);
                } else {
                    int days = (int) Math.pow(2, reviewCount);
                    userWord.setNextReviewTime(LocalDateTime.now().plusDays(days));
                }
            } else {
                userWord.setNextReviewTime(LocalDateTime.now().plusMinutes(30));
            }
            
            userWordMapper.update(userWord);
            userProgressService.updateWordProgress(userId, false, userWord.getIsMastered() == 1);
        }
        
        return userWord;
    }

    @Override
    public List<UserWord> getNewWords(Long userId, int limit) {
        return userWordMapper.selectNewWords(userId, limit);
    }

    @Override
    public List<UserWord> getReviewWords(Long userId, int limit) {
        return userWordMapper.selectReviewWords(userId, LocalDateTime.now(), limit);
    }

    @Override
    public List<UserWord> getLearnedWords(Long userId, int limit) {
        return userWordMapper.selectLearnedWords(userId, limit);
    }

    @Override
    public List<UserWord> getMasteredWords(Long userId, int limit) {
        return userWordMapper.selectMasteredWords(userId, limit);
    }

    @Override
    public int countNewWords(Long userId) {
        return userWordMapper.countNewWords(userId);
    }

    @Override
    public int countReviewWords(Long userId) {
        return userWordMapper.countReviewWords(userId, LocalDateTime.now());
    }

    @Override
    public int countLearnedWords(Long userId) {
        return userWordMapper.countLearnedWords(userId);
    }

    @Override
    public int countMasteredWords(Long userId) {
        return userWordMapper.countMasteredWords(userId);
    }
}
