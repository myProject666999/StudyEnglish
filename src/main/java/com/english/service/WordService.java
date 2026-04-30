package com.english.service;

import com.english.entity.Word;
import com.english.entity.WordLevel;
import com.english.entity.UserWord;

import java.util.List;

public interface WordService {

    Word findById(Long id);

    WordLevel findLevelById(Long id);

    List<WordLevel> findAllLevels();

    List<Word> findByLevelId(Long levelId);

    List<Word> findRandomWords(Long levelId, int limit);

    List<Word> findList(String keyword, Long levelId);

    boolean addWord(Word word);

    boolean updateWord(Word word);

    boolean deleteWord(Long id);

    boolean addLevel(WordLevel wordLevel);

    boolean updateLevel(WordLevel wordLevel);

    boolean deleteLevel(Long id);

    UserWord startLearning(Long userId, Long wordId);

    UserWord markAsLearned(Long userId, Long wordId);

    UserWord markAsMastered(Long userId, Long wordId);

    UserWord addToNewWords(Long userId, Long wordId);

    UserWord reviewWord(Long userId, Long wordId, boolean isCorrect);

    List<UserWord> getNewWords(Long userId, int limit);

    List<UserWord> getReviewWords(Long userId, int limit);

    List<UserWord> getLearnedWords(Long userId, int limit);

    List<UserWord> getMasteredWords(Long userId, int limit);

    int countNewWords(Long userId);

    int countReviewWords(Long userId);

    int countLearnedWords(Long userId);

    int countMasteredWords(Long userId);
}
