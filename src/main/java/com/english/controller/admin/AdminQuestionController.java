package com.english.controller.admin;

import com.english.entity.Question;
import com.english.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("")
    public String questionList(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Integer questionType,
                              @RequestParam(required = false) Integer difficulty,
                              Model model) {
        List<Question> questions = questionService.findList(keyword, questionType, difficulty);
        model.addAttribute("questions", questions);
        model.addAttribute("keyword", keyword);
        model.addAttribute("questionType", questionType);
        model.addAttribute("difficulty", difficulty);
        
        return "admin/question-list";
    }

    @GetMapping("/add")
    public String addQuestionPage() {
        return "admin/question-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addQuestion(@RequestParam Integer questionType,
                                           @RequestParam String question,
                                           @RequestParam(required = false) String optionA,
                                           @RequestParam(required = false) String optionB,
                                           @RequestParam(required = false) String optionC,
                                           @RequestParam(required = false) String optionD,
                                           @RequestParam(required = false) String options,
                                           @RequestParam String answer,
                                           @RequestParam(required = false) String analysis,
                                           @RequestParam(defaultValue = "1") Integer difficulty) {
        Map<String, Object> result = new HashMap<>();
        
        Question newQuestion = new Question();
        newQuestion.setQuestionType(questionType);
        newQuestion.setQuestion(question);
        newQuestion.setOptionA(optionA);
        newQuestion.setOptionB(optionB);
        newQuestion.setOptionC(optionC);
        newQuestion.setOptionD(optionD);
        newQuestion.setOptions(options);
        newQuestion.setAnswer(answer);
        newQuestion.setAnalysis(analysis);
        newQuestion.setDifficulty(difficulty);
        newQuestion.setStatus(1);
        
        boolean success = questionService.addQuestion(newQuestion);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/edit/{id}")
    public String editQuestionPage(@PathVariable Long id, Model model) {
        Question question = questionService.findById(id);
        model.addAttribute("question", question);
        return "admin/question-edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editQuestion(@RequestParam Long id,
                                            @RequestParam(required = false) Integer questionType,
                                            @RequestParam(required = false) String question,
                                            @RequestParam(required = false) String optionA,
                                            @RequestParam(required = false) String optionB,
                                            @RequestParam(required = false) String optionC,
                                            @RequestParam(required = false) String optionD,
                                            @RequestParam(required = false) String options,
                                            @RequestParam(required = false) String answer,
                                            @RequestParam(required = false) String analysis,
                                            @RequestParam(required = false) Integer difficulty,
                                            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        
        if (questionType != null) {
            updateQuestion.setQuestionType(questionType);
        }
        if (question != null) {
            updateQuestion.setQuestion(question);
        }
        if (optionA != null) {
            updateQuestion.setOptionA(optionA);
        }
        if (optionB != null) {
            updateQuestion.setOptionB(optionB);
        }
        if (optionC != null) {
            updateQuestion.setOptionC(optionC);
        }
        if (optionD != null) {
            updateQuestion.setOptionD(optionD);
        }
        if (options != null) {
            updateQuestion.setOptions(options);
        }
        if (answer != null) {
            updateQuestion.setAnswer(answer);
        }
        if (analysis != null) {
            updateQuestion.setAnalysis(analysis);
        }
        if (difficulty != null) {
            updateQuestion.setDifficulty(difficulty);
        }
        if (status != null) {
            updateQuestion.setStatus(status);
        }
        
        boolean success = questionService.updateQuestion(updateQuestion);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteQuestion(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = questionService.deleteQuestion(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
