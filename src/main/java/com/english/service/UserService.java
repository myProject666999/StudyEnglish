package com.english.service;

import com.english.entity.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    List<User> findList(String keyword, Integer role);

    boolean register(User user);

    boolean login(String username, String password);

    User adminLogin(String username, String password);

    boolean updatePassword(Long id, String oldPassword, String newPassword);

    boolean resetPasswordByEmail(String email, String newPassword);

    boolean update(User user);

    boolean delete(Long id);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
}
