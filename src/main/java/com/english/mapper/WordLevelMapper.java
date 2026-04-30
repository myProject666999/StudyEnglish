package com.english.mapper;

import com.english.entity.WordLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WordLevelMapper {

    WordLevel selectById(@Param("id") Long id);

    List<WordLevel> selectAll();

    List<WordLevel> selectList(@Param("keyword") String keyword);

    int insert(WordLevel wordLevel);

    int update(WordLevel wordLevel);

    int deleteById(@Param("id") Long id);
}
