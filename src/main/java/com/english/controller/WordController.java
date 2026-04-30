package com.english.controller;

import com.english.entity.User;
import com.english.entity.Word;
import com.english.entity.WordLevel;
import com.english.entity.UserWord;
import com.english.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping("/levels")
    public String listLevels(Model model) {
        List<WordLevel> levels = wordService.findAllLevels();
        model.addAttribute("levels", levels);
        return "word/levels";
    }

    @GetMapping("/level/{levelId}")
    public String listWordsByLevel(@PathVariable Long levelId, Model model) {
        List<Word> words = wordService.findByLevelId(levelId);
        WordLevel level = wordService.findAllLevels().stream()
                .filter(l -> l.getId().equals(levelId))
                .findFirst()
                .orElse(null);
        model.addAttribute("words", words);
        model.addAttribute("level", level);
        return "word/word-list";
    }

    @GetMapping("/learn")
    public String learnPage(@RequestParam(required = false) Long levelId, 
                           @RequestParam(defaultValue = "10") int count,
                           Model model,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        List<WordLevel> levels = wordService.findAllLevels();
        model.addAttribute("levels", levels);
        
        if (levelId == null && !levels.isEmpty()) {
            levelId = levels.get(0).getId();
        }
        
        if (levelId != null) {
            List<Word> words = wordService.findRandomWords(levelId, count);
            model.addAttribute("words", words);
            model.addAttribute("selectedLevelId", levelId);
        }
        
        int newCount = wordService.countNewWords(user.getId());
        int reviewCount = wordService.countReviewWords(user.getId());
        model.addAttribute("newWordsCount", newCount);
        model.addAttribute("reviewWordsCount", reviewCount);
        
        return "word/learn";
    }

    @GetMapping("/new-words")
    public String newWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> newWords = wordService.getNewWords(user.getId(), 20);
        model.addAttribute("newWords", newWords);
        model.addAttribute("count", newWords.size());
        return "word/new-words";
    }

    @GetMapping("/review-words")
    public String reviewWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> reviewWords = wordService.getReviewWords(user.getId(), 20);
        model.addAttribute("reviewWords", reviewWords);
        model.addAttribute("count", reviewWords.size());
        return "word/review-words";
    }

    @GetMapping("/learned-words")
    public String learnedWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> learnedWords = wordService.getLearnedWords(user.getId(), 20);
        model.addAttribute("learnedWords", learnedWords);
        model.addAttribute("count", learnedWords.size());
        return "word/learned-words";
    }

    @GetMapping("/mastered-words")
    public String masteredWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> masteredWords = wordService.getMasteredWords(user.getId(), 20);
        model.addAttribute("masteredWords", masteredWords);
        model.addAttribute("count", masteredWords.size());
        return "word/mastered-words";
    }

    @PostMapping("/mark-learned")
    @ResponseBody
    public Map<String, Object> markAsLearned(@RequestParam Long wordId, 
                                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserWord userWord = wordService.markAsLearned(user.getId(), wordId);
            result.put("success", true);
            result.put("message", "标记成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/mark-mastered")
    @ResponseBody
    public Map<String, Object> markAsMastered(@RequestParam Long wordId,
                                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserWord userWord = wordService.markAsMastered(user.getId(), wordId);
            result.put("success", true);
            result.put("message", "已标记为已掌握");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/add-new-word")
    @ResponseBody
    public Map<String, Object> addToNewWords(@RequestParam Long wordId,
                                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserWord userWord = wordService.addToNewWords(user.getId(), wordId);
            result.put("success", true);
            result.put("message", "已加入生词本");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/review")
    @ResponseBody
    public Map<String, Object> reviewWord(@RequestParam Long wordId,
                                          @RequestParam boolean isCorrect,
                                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserWord userWord = wordService.reviewWord(user.getId(), wordId, isCorrect);
            result.put("success", true);
            result.put("isMastered", userWord.getIsMastered() == 1);
            result.put("reviewCount", userWord.getReviewCount());
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
        
        stats.put("newWords", wordService.countNewWords(user.getId()));
        stats.put("reviewWords", wordService.countReviewWords(user.getId()));
        stats.put("learnedWords", wordService.countLearnedWords(user.getId()));
        stats.put("masteredWords", wordService.countMasteredWords(user.getId()));
        
        return stats;
    }
}
