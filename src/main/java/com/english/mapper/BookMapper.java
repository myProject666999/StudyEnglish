package com.english.mapper;

import com.english.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {

    Book selectById(@Param("id") Long id);

    List<Book> selectAll();

    List<Book> selectList(@Param("keyword") String keyword, @Param("category") String category);

    int insert(Book book);

    int update(Book book);

    int deleteById(@Param("id") Long id);

    int countAll();
}
