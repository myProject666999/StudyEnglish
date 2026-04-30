package com.english.mapper;

import com.english.entity.Listening;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ListeningMapper {

    Listening selectById(@Param("id") Long id);

    List<Listening> selectAll();

    List<Listening> selectList(@Param("keyword") String keyword, @Param("difficulty") Integer difficulty);

    int insert(Listening listening);

    int update(Listening listening);

    int deleteById(@Param("id") Long id);

    int countAll();
}
