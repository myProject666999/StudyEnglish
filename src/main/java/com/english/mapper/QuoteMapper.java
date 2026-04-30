package com.english.mapper;

import com.english.entity.Quote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuoteMapper {

    Quote selectById(@Param("id") Long id);

    Quote selectDaily();

    Quote selectRandom();

    List<Quote> selectAll();

    List<Quote> selectList(@Param("keyword") String keyword);

    int insert(Quote quote);

    int update(Quote quote);

    int deleteById(@Param("id") Long id);

    int updateDailyStatus();
}
