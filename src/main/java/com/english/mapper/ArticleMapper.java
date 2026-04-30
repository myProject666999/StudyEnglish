package com.english.mapper;

import com.english.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    Article selectById(@Param("id") Long id);

    List<Article> selectByBookId(@Param("bookId") Long bookId);

    List<Article> selectList(@Param("keyword") String keyword, @Param("bookId") Long bookId, @Param("difficulty") Integer difficulty);

    int insert(Article article);

    int update(Article article);

    int deleteById(@Param("id") Long id);

    int countAll();
}
