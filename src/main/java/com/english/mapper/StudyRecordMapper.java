package com.english.mapper;

import com.english.entity.StudyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StudyRecordMapper {

    StudyRecord selectById(@Param("id") Long id);

    List<StudyRecord> selectByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    List<StudyRecord> selectByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    List<StudyRecord> selectByType(@Param("userId") Long userId, @Param("recordType") Integer recordType, @Param("limit") Integer limit);

    int insert(StudyRecord studyRecord);

    int deleteByUserId(@Param("userId") Long userId);

    int countByUserId(@Param("userId") Long userId);

    int countByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
