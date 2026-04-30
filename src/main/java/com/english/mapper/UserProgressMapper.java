package com.english.mapper;

import com.english.entity.UserProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProgressMapper {

    UserProgress selectById(@Param("id") Long id);

    UserProgress selectByUserId(@Param("userId") Long userId);

    int insert(UserProgress userProgress);

    int update(UserProgress userProgress);

    int deleteByUserId(@Param("userId") Long userId);
}
