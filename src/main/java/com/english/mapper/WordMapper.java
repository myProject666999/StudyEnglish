package com.english.mapper;

import com.english.entity.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WordMapper {

    Word selectById(@Param("id") Long id);

    List<Word> selectByLevelId(@Param("levelId") Long levelId);

    List<Word> selectList(@Param("keyword") String keyword, @Param("levelId") Long levelId);

    List<Word> selectRandom(@Param("levelId") Long levelId, @Param("limit") Integer limit);

    List<Word> selectByWordIds(@Param("wordIds") List<Long> wordIds);

    int insert(Word word);

    int update(Word word);

    int deleteById(@Param("id") Long id);

    int countByLevelId(@Param("levelId") Long levelId);

    int countAll();
}
