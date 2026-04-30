package com.english.mapper;

import com.english.entity.UserWord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserWordMapper {

    UserWord selectById(@Param("id") Long id);

    UserWord selectByUserAndWord(@Param("userId") Long userId, @Param("wordId") Long wordId);

    List<UserWord> selectNewWords(@Param("userId") Long userId, @Param("limit") Integer limit);

    List<UserWord> selectReviewWords(@Param("userId") Long userId, @Param("now") LocalDateTime now, @Param("limit") Integer limit);

    List<UserWord> selectLearnedWords(@Param("userId") Long userId, @Param("limit") Integer limit);

    List<UserWord> selectMasteredWords(@Param("userId") Long userId, @Param("limit") Integer limit);

    int insert(UserWord userWord);

    int update(UserWord userWord);

    int deleteByUserId(@Param("userId") Long userId);

    int countNewWords(@Param("userId") Long userId);

    int countReviewWords(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    int countLearnedWords(@Param("userId") Long userId);

    int countMasteredWords(@Param("userId") Long userId);
}
