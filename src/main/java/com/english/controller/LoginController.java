package com.english.controller;

import com.english.entity.User;
import com.english.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping({"/", "/index"})
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getRole() == 1) {
                return "redirect:/admin/index";
            }
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session,
                       Model model) {
        User user = userService.findByUsername(username);
        
        if (user == null) {
            model.addAttribute("error", "用户不存在");
            return "login";
        }
        
        if (user.getStatus() != 1) {
            model.addAttribute("error", "账户已被禁用");
            return "login";
        }
        
        boolean success = userService.login(username, password);
        
        if (success) {
            session.setAttribute("user", user);
            if (user.getRole() == 1) {
                return "redirect:/admin/index";
            }
            return "redirect:/home";
        } else {
            model.addAttribute("error", "密码错误");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String nickname,
                          Model model) {
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次密码输入不一致");
            return "register";
        }
        
        if (userService.checkUsernameExists(username)) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setNickname(nickname != null && !nickname.isEmpty() ? nickname : username);
        
        boolean success = userService.register(user);
        
        if (success) {
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "注册失败，请稍后重试");
            return "register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次密码输入不一致");
            return "forgot-password";
        }
        
        if (!userService.checkEmailExists(email)) {
            model.addAttribute("error", "该邮箱未注册");
            return "forgot-password";
        }
        
        boolean success = userService.resetPasswordByEmail(email, newPassword);
        
        if (success) {
            model.addAttribute("success", "密码重置成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "密码重置失败，请稍后重试");
            return "forgot-password";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return userService.checkUsernameExists(username);
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        return userService.checkEmailExists(email);
    }
}
