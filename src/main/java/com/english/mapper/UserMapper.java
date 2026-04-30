package com.english.mapper;

import com.english.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(@Param("id") Long id);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    User selectByPhone(@Param("phone") String phone);

    List<User> selectList(@Param("keyword") String keyword, @Param("role") Integer role);

    List<User> selectRecent(@Param("limit") Integer limit);

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int countAll();
}
