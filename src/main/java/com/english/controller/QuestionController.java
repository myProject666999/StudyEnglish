package com.english.controller;

import com.english.entity.User;
import com.english.entity.Question;
import com.english.entity.UserQuestion;
import com.english.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/practice")
    public String practicePage(@RequestParam(required = false) Integer questionType,
                               @RequestParam(defaultValue = "10") int count,
                               Model model) {
        if (questionType == null) {
            questionType = 1;
        }
        
        List<Question> questions = questionService.findRandomQuestions(questionType, count);
        model.addAttribute("questions", questions);
        model.addAttribute("questionType", questionType);
        model.addAttribute("count", questions.size());
        
        return "question/practice";
    }

    @GetMapping("/wrong-book")
    public String wrongBook(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserQuestion> wrongQuestions = questionService.getWrongBook(user.getId(), 20);
        model.addAttribute("wrongQuestions", wrongQuestions);
        model.addAttribute("count", wrongQuestions.size());
        
        return "question/wrong-book";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserQuestion> history = questionService.getUserHistory(user.getId(), 20);
        model.addAttribute("history", history);
        model.addAttribute("count", history.size());
        
        return "question/history";
    }

    @PostMapping("/submit")
    @ResponseBody
    public Map<String, Object> submitAnswer(@RequestParam Long questionId,
                                            @RequestParam String userAnswer,
                                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserQuestion userQuestion = questionService.submitAnswer(user.getId(), questionId, userAnswer);
            Question question = questionService.findById(questionId);
            
            result.put("success", true);
            result.put("isCorrect", userQuestion.getIsCorrect() == 1);
            result.put("correctAnswer", question.getAnswer());
            result.put("analysis", question.getAnalysis());
            result.put("userAnswer", userAnswer);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "提交失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/add-wrong-book")
    @ResponseBody
    public Map<String, Object> addToWrongBook(@RequestParam Long questionId,
                                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            questionService.addToWrongBook(user.getId(), questionId);
            result.put("success", true);
            result.put("message", "已加入错题本");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/remove-wrong-book")
    @ResponseBody
    public Map<String, Object> removeFromWrongBook(@RequestParam Long questionId,
                                                    HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            questionService.removeFromWrongBook(user.getId(), questionId);
            result.put("success", true);
            result.put("message", "已从错题本移除");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/clear-wrong-book")
    @ResponseBody
    public Map<String, Object> clearWrongBook(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            questionService.clearWrongBook(user.getId());
            result.put("success", true);
            result.put("message", "错题本已清空");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("wrongBookCount", questionService.countWrongBook(user.getId()));
        stats.put("historyCount", questionService.countUserHistory(user.getId()));
        
        return stats;
    }
}
