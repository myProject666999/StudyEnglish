package com.english.controller;

import com.english.entity.User;
import com.english.entity.Quote;
import com.english.service.QuoteService;
import com.english.service.UserProgressService;
import com.english.service.WordService;
import com.english.service.QuestionService;
import com.english.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private UserProgressService userProgressService;

    @Autowired
    private WordService wordService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        Quote dailyQuote = quoteService.getDailyQuote();
        model.addAttribute("dailyQuote", dailyQuote);
        
        Map<String, Object> progressStats = userProgressService.getStatistics(user.getId());
        model.addAttribute("progressStats", progressStats);
        
        int newWords = wordService.countNewWords(user.getId());
        int reviewWords = wordService.countReviewWords(user.getId());
        int wrongQuestions = questionService.countWrongBook(user.getId());
        
        model.addAttribute("newWords", newWords);
        model.addAttribute("reviewWords", reviewWords);
        model.addAttribute("wrongQuestions", wrongQuestions);
        
        return "home";
    }

    @GetMapping("/progress")
    public String progress(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        Map<String, Object> stats = userProgressService.getStatistics(user.getId());
        model.addAttribute("stats", stats);
        
        return "progress";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        return "profile";
    }

    @PostMapping("/profile/update")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestParam(required = false) String nickname,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) String phone,
                                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            User updateUser = new User();
            updateUser.setId(user.getId());
            
            if (nickname != null && !nickname.isEmpty()) {
                updateUser.setNickname(nickname);
            }
            if (email != null && !email.isEmpty()) {
                updateUser.setEmail(email);
            }
            if (phone != null && !phone.isEmpty()) {
                updateUser.setPhone(phone);
            }
            
            User currentUser = (User) session.getAttribute("user");
            if (nickname != null) {
                currentUser.setNickname(nickname);
            }
            if (email != null) {
                currentUser.setEmail(email);
            }
            if (phone != null) {
                currentUser.setPhone(phone);
            }
            session.setAttribute("user", currentUser);
            
            result.put("success", true);
            result.put("message", "资料更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/profile/change-password")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestParam String oldPassword,
                                               @RequestParam String newPassword,
                                               @RequestParam String confirmPassword,
                                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        if (!newPassword.equals(confirmPassword)) {
            result.put("success", false);
            result.put("message", "两次密码输入不一致");
            return result;
        }
        
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            result.put("success", false);
            result.put("message", "密码长度需在6-20字符之间");
            return result;
        }
        
        try {
            boolean success = userService.updatePassword(user.getId(), oldPassword, newPassword);
            if (success) {
                result.put("success", true);
                result.put("message", "密码修改成功");
            } else {
                result.put("success", false);
                result.put("message", "原密码错误");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "修改失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/clear-progress")
    @ResponseBody
    public Map<String, Object> clearProgress(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            userProgressService.clearAllProgress(user.getId());
            result.put("success", true);
            result.put("message", "学习记录已清空");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }
}
