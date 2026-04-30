package com.english.controller;

import com.english.entity.User;
import com.english.entity.Listening;
import com.english.service.ListeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/listenings")
public class ListeningController {

    @Autowired
    private ListeningService listeningService;

    @GetMapping("")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer difficulty,
                       Model model) {
        List<Listening> listenings = listeningService.findList(keyword, difficulty);
        model.addAttribute("listenings", listenings);
        model.addAttribute("keyword", keyword);
        model.addAttribute("difficulty", difficulty);
        
        return "listenings/list";
    }

    @GetMapping("/play/{id}")
    public String play(@PathVariable Long id, Model model) {
        Listening listening = listeningService.findById(id);
        model.addAttribute("listening", listening);
        
        return "listenings/list";
    }

    @PostMapping("/mark-complete")
    @ResponseBody
    public Map<String, Object> markAsCompleted(@RequestParam Long listeningId,
                                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            listeningService.markAsCompleted(user.getId(), listeningId);
            result.put("success", true);
            result.put("message", "已标记为完成");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }
}
