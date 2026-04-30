package com.english.controller.admin;

import com.english.entity.User;
import com.english.mapper.*;
import com.english.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        User admin = userService.adminLogin(username, password);
        
        if (admin == null) {
            model.addAttribute("error", "用户名或密码错误，或不是管理员账户");
            return "admin/login";
        }
        
        session.setAttribute("admin", admin);
        return "redirect:/admin/index";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || admin.getRole() != 1) {
            return "redirect:/admin/login";
        }
        
        int totalUsers = userMapper.countAll();
        int totalWords = wordMapper.countAll();
        int totalArticles = articleMapper.countAll();
        int totalQuestions = questionMapper.countAll();
        List<User> recentUsers = userMapper.selectRecent(10);
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalWords", totalWords);
        model.addAttribute("totalArticles", totalArticles);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("recentUsers", recentUsers);
        
        return "admin/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/users")
    public String userList(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Integer role,
                          Model model) {
        List<User> users = userService.findList(keyword, role);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);
        return "admin/users";
    }

    @GetMapping("/users/add")
    public String addUserPage() {
        return "admin/users";
    }

    @PostMapping("/users/add")
    @ResponseBody
    public Map<String, Object> addUser(@RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String nickname,
                                       @RequestParam(defaultValue = "0") Integer role) {
        Map<String, Object> result = new HashMap<>();
        
        if (userService.checkUsernameExists(username)) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(nickname != null && !nickname.isEmpty() ? nickname : username);
        user.setRole(role);
        user.setStatus(1);
        
        boolean success = userService.register(user);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/users/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/users";
    }

    @PostMapping("/users/edit")
    @ResponseBody
    public Map<String, Object> editUser(@RequestParam Long id,
                                        @RequestParam(required = false) String password,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String phone,
                                        @RequestParam(required = false) String nickname,
                                        @RequestParam(required = false) Integer role,
                                        @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        User user = new User();
        user.setId(id);
        
        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (role != null) {
            user.setRole(role);
        }
        if (status != null) {
            user.setStatus(status);
        }
        
        boolean success = userService.update(user);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/users/delete")
    @ResponseBody
    public Map<String, Object> deleteUser(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = userService.delete(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @PostMapping("/users/reset-password")
    @ResponseBody
    public Map<String, Object> resetPassword(@RequestParam Long id,
                                              @RequestParam String newPassword) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = userService.findById(id);
            if (user != null) {
                userService.updatePassword(user.getId(), user.getPassword(), newPassword);
                result.put("success", true);
                result.put("message", "密码重置成功");
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }
}
