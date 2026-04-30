package com.english.service.impl;

import com.english.entity.User;
import com.english.mapper.UserMapper;
import com.english.mapper.UserProgressMapper;
import com.english.entity.UserProgress;
import com.english.service.UserService;
import com.english.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProgressMapper userProgressMapper;

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public List<User> findList(String keyword, Integer role) {
        return userMapper.selectList(keyword, role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        if (checkUsernameExists(user.getUsername())) {
            return false;
        }
        user.setPassword(MD5Util.md5(user.getPassword()));
        user.setRole(0);
        user.setStatus(1);
        int result = userMapper.insert(user);
        
        if (result > 0) {
            UserProgress progress = new UserProgress();
            progress.setUserId(user.getId());
            progress.setTotalWords(0);
            progress.setLearnedWords(0);
            progress.setMasteredWords(0);
            progress.setTotalArticles(0);
            progress.setReadArticles(0);
            progress.setTotalListenings(0);
            progress.setCompletedListenings(0);
            progress.setTotalQuestions(0);
            progress.setCorrectQuestions(0);
            progress.setStudyDays(0);
            progress.setContinuousDays(0);
            progress.setLastStudyDate(LocalDate.now());
            userProgressMapper.insert(progress);
        }
        
        return result > 0;
    }

    @Override
    public boolean login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return false;
        }
        if (user.getStatus() != 1) {
            return false;
        }
        return MD5Util.verify(password, user.getPassword());
    }

    @Override
    public User adminLogin(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getRole() != 1) {
            return null;
        }
        if (user.getStatus() != 1) {
            return null;
        }
        if (!MD5Util.verify(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return false;
        }
        if (!MD5Util.verify(oldPassword, user.getPassword())) {
            return false;
        }
        return userMapper.updatePassword(id, MD5Util.md5(newPassword)) > 0;
    }

    @Override
    public boolean resetPasswordByEmail(String email, String newPassword) {
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            return false;
        }
        return userMapper.updatePassword(user.getId(), MD5Util.md5(newPassword)) > 0;
    }

    @Override
    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userMapper.selectByEmail(email) != null;
    }
}
