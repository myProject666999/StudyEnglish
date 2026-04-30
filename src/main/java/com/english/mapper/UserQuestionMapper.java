package com.english.mapper;

import com.english.entity.UserQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserQuestionMapper {

    UserQuestion selectById(@Param("id") Long id);

    UserQuestion selectByUserAndQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    List<UserQuestion> selectWrongBook(@Param("userId") Long userId, @Param("limit") Integer limit);

    List<UserQuestion> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    int insert(UserQuestion userQuestion);

    int update(UserQuestion userQuestion);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteWrongBook(@Param("userId") Long userId);

    int countWrongBook(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);
}
