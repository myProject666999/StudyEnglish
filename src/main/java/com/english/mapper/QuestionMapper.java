package com.english.mapper;

import com.english.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    Question selectById(@Param("id") Long id);

    List<Question> selectByType(@Param("questionType") Integer questionType);

    List<Question> selectList(@Param("keyword") String keyword, @Param("questionType") Integer questionType, @Param("difficulty") Integer difficulty);

    List<Question> selectRandom(@Param("questionType") Integer questionType, @Param("limit") Integer limit);

    int insert(Question question);

    int update(Question question);

    int deleteById(@Param("id") Long id);

    int countAll();
}
