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
            
            if (words != null && !words.isEmpty()) {
                model.addAttribute("word", words.get(0));
                model.addAttribute("currentIndex", 0);
                model.addAttribute("totalWords", words.size());
            }
        }
        
        int newCount = wordService.countNewWords(user.getId());
        int reviewCount = wordService.countReviewWords(user.getId());
        model.addAttribute("newWordsCount", newCount);
        model.addAttribute("reviewWordsCount", reviewCount);
        
        return "words/learn";
    }

    @PostMapping("/learn/{levelId}/next")
    public String learnNext(@PathVariable Long levelId,
                            @RequestParam Long wordId,
                            @RequestParam int currentIndex,
                            @RequestParam int totalWords,
                            @RequestParam String action,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if ("vocabulary".equals(action)) {
            wordService.addToNewWords(user.getId(), wordId);
        } else if ("known".equals(action)) {
            wordService.markAsLearned(user.getId(), wordId);
        }
        
        int nextIndex = currentIndex + 1;
        if (nextIndex < totalWords) {
            return "redirect:/words/learn/" + levelId + "?index=" + nextIndex;
        }
        
        return "redirect:/words/learn";
    }

    @GetMapping("/learn/{levelId}")
    public String learnWithIndex(@PathVariable Long levelId,
                                 @RequestParam(required = false, defaultValue = "0") int index,
                                 @RequestParam(defaultValue = "10") int count,
                                 Model model,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        List<WordLevel> levels = wordService.findAllLevels();
        model.addAttribute("levels", levels);
        model.addAttribute("selectedLevelId", levelId);
        
        List<Word> words = wordService.findRandomWords(levelId, count);
        model.addAttribute("words", words);
        
        if (words != null && index < words.size()) {
            model.addAttribute("word", words.get(index));
            model.addAttribute("currentIndex", index);
            model.addAttribute("totalWords", words.size());
        }
        
        int newCount = wordService.countNewWords(user.getId());
        int reviewCount = wordService.countReviewWords(user.getId());
        model.addAttribute("newWordsCount", newCount);
        model.addAttribute("reviewWordsCount", reviewCount);
        
        return "words/learn";
    }

    @GetMapping("/review")
    public String reviewPage(@RequestParam(required = false, defaultValue = "0") int index,
                             Model model,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        List<UserWord> reviewWords = wordService.getReviewWords(user.getId(), 20);
        model.addAttribute("words", reviewWords);
        
        if (reviewWords != null && !reviewWords.isEmpty() && index < reviewWords.size()) {
            UserWord userWord = reviewWords.get(index);
            Word word = wordService.findById(userWord.getWordId());
            model.addAttribute("word", word);
            model.addAttribute("userWordId", userWord.getId());
            model.addAttribute("currentIndex", index);
        }
        
        return "words/review";
    }

    @PostMapping("/review/submit")
    public String reviewSubmit(@RequestParam(required = false) Long userWordId,
                               @RequestParam int currentIndex,
                               @RequestParam int grade,
                               Model model,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        boolean isCorrect = grade >= 3;
        
        if (userWordId != null) {
            List<UserWord> reviewWords = wordService.getReviewWords(user.getId(), 20);
            if (reviewWords != null && currentIndex < reviewWords.size()) {
                UserWord userWord = reviewWords.get(currentIndex);
                wordService.reviewWord(user.getId(), userWord.getWordId(), isCorrect);
            }
        }
        
        return "redirect:/words/review?index=" + (currentIndex + 1);
    }

    @GetMapping("/vocabulary")
    public String vocabularyPage(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        List<UserWord> newWords = wordService.getNewWords(user.getId(), size);
        model.addAttribute("words", newWords);
        model.addAttribute("totalWords", newWords != null ? newWords.size() : 0);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        
        return "words/vocabulary";
    }

    @GetMapping("/vocabulary/master/{wordId}")
    public String markAsMastered(@PathVariable Long wordId,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        wordService.markAsMastered(user.getId(), wordId);
        return "redirect:/words/vocabulary";
    }

    @GetMapping("/vocabulary/remove/{wordId}")
    public String removeFromVocabulary(@PathVariable Long wordId,
                                        HttpSession session) {
        User user = (User) session.getAttribute("user");
        wordService.removeFromNewWords(user.getId(), wordId);
        return "redirect:/words/vocabulary";
    }

    @GetMapping("/learned-words")
    public String learnedWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> learnedWords = wordService.getLearnedWords(user.getId(), 20);
        model.addAttribute("learnedWords", learnedWords);
        model.addAttribute("count", learnedWords.size());
        return "words/learn";
    }

    @GetMapping("/mastered-words")
    public String masteredWords(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<UserWord> masteredWords = wordService.getMasteredWords(user.getId(), 20);
        model.addAttribute("masteredWords", masteredWords);
        model.addAttribute("count", masteredWords.size());
        return "words/learn";
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
