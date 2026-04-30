package com.english.controller.admin;

import com.english.entity.Word;
import com.english.entity.WordLevel;
import com.english.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/words")
public class AdminWordController {

    @Autowired
    private WordService wordService;

    @GetMapping("/levels")
    public String levelList(Model model) {
        List<WordLevel> levels = wordService.findAllLevels();
        model.addAttribute("levels", levels);
        return "admin/level-list";
    }

    @GetMapping("/levels/add")
    public String addLevelPage() {
        return "admin/level-add";
    }

    @PostMapping("/levels/add")
    @ResponseBody
    public Map<String, Object> addLevel(@RequestParam String name,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(defaultValue = "0") Integer sort) {
        Map<String, Object> result = new HashMap<>();
        
        WordLevel level = new WordLevel();
        level.setName(name);
        level.setDescription(description);
        level.setSort(sort);
        level.setStatus(1);
        
        List<WordLevel> allLevels = wordService.findAllLevels();
        boolean exists = allLevels.stream().anyMatch(l -> l.getName().equals(name));
        
        if (exists) {
            result.put("success", false);
            result.put("message", "等级名称已存在");
            return result;
        }
        
        boolean success = wordService.addLevel(level);
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/levels/edit/{id}")
    public String editLevelPage(@PathVariable Long id, Model model) {
        WordLevel level = wordService.findLevelById(id);
        model.addAttribute("level", level);
        return "admin/level-edit";
    }

    @PostMapping("/levels/edit")
    @ResponseBody
    public Map<String, Object> editLevel(@RequestParam Long id,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String description,
                                          @RequestParam(required = false) Integer sort,
                                          @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        WordLevel level = new WordLevel();
        level.setId(id);
        
        if (name != null) {
            level.setName(name);
        }
        if (description != null) {
            level.setDescription(description);
        }
        if (sort != null) {
            level.setSort(sort);
        }
        if (status != null) {
            level.setStatus(status);
        }
        
        boolean success = wordService.updateLevel(level);
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/levels/delete")
    @ResponseBody
    public Map<String, Object> deleteLevel(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = wordService.deleteLevel(id);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("")
    public String wordList(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Long levelId,
                          Model model) {
        List<WordLevel> levels = wordService.findAllLevels();
        List<Word> words = wordService.findList(keyword, levelId);
        
        model.addAttribute("words", words);
        model.addAttribute("levels", levels);
        model.addAttribute("keyword", keyword);
        model.addAttribute("levelId", levelId);
        
        return "admin/word-list";
    }

    @GetMapping("/add")
    public String addWordPage(Model model) {
        List<WordLevel> levels = wordService.findAllLevels();
        model.addAttribute("levels", levels);
        return "admin/word-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addWord(@RequestParam Long levelId,
                                       @RequestParam String word,
                                       @RequestParam(required = false) String phonetic,
                                       @RequestParam(required = false) String meaning,
                                       @RequestParam(required = false) String example,
                                       @RequestParam(required = false) String exampleMeaning,
                                       @RequestParam(defaultValue = "0") Integer sort) {
        Map<String, Object> result = new HashMap<>();
        
        Word newWord = new Word();
        newWord.setLevelId(levelId);
        newWord.setWord(word);
        newWord.setPhonetic(phonetic);
        newWord.setMeaning(meaning);
        newWord.setExample(example);
        newWord.setExampleMeaning(exampleMeaning);
        newWord.setSort(sort);
        newWord.setStatus(1);
        
        boolean success = wordService.addWord(newWord);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/edit/{id}")
    public String editWordPage(@PathVariable Long id, Model model) {
        Word word = wordService.findById(id);
        List<WordLevel> levels = wordService.findAllLevels();
        
        model.addAttribute("word", word);
        model.addAttribute("levels", levels);
        return "admin/word-edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editWord(@RequestParam Long id,
                                        @RequestParam(required = false) Long levelId,
                                        @RequestParam(required = false) String word,
                                        @RequestParam(required = false) String phonetic,
                                        @RequestParam(required = false) String meaning,
                                        @RequestParam(required = false) String example,
                                        @RequestParam(required = false) String exampleMeaning,
                                        @RequestParam(required = false) Integer sort,
                                        @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        Word updateWord = new Word();
        updateWord.setId(id);
        
        if (levelId != null) {
            updateWord.setLevelId(levelId);
        }
        if (word != null) {
            updateWord.setWord(word);
        }
        if (phonetic != null) {
            updateWord.setPhonetic(phonetic);
        }
        if (meaning != null) {
            updateWord.setMeaning(meaning);
        }
        if (example != null) {
            updateWord.setExample(example);
        }
        if (exampleMeaning != null) {
            updateWord.setExampleMeaning(exampleMeaning);
        }
        if (sort != null) {
            updateWord.setSort(sort);
        }
        if (status != null) {
            updateWord.setStatus(status);
        }
        
        boolean success = wordService.updateWord(updateWord);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteWord(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = wordService.deleteWord(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
