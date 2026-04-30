package com.english.controller.admin;

import com.english.entity.Listening;
import com.english.service.ListeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/listening")
public class AdminListeningController {

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
        
        return "admin/listening-list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "admin/listening-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> add(@RequestParam String title,
                                    @RequestParam(required = false) String description,
                                    @RequestParam String audioUrl,
                                    @RequestParam(required = false) String transcript,
                                    @RequestParam(required = false) String translation,
                                    @RequestParam(defaultValue = "1") Integer difficulty,
                                    @RequestParam(defaultValue = "0") Integer duration) {
        Map<String, Object> result = new HashMap<>();
        
        Listening listening = new Listening();
        listening.setTitle(title);
        listening.setDescription(description);
        listening.setAudioUrl(audioUrl);
        listening.setTranscript(transcript);
        listening.setTranslation(translation);
        listening.setDifficulty(difficulty);
        listening.setDuration(duration);
        listening.setStatus(1);
        
        boolean success = listeningService.addListening(listening);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Listening listening = listeningService.findById(id);
        model.addAttribute("listening", listening);
        return "admin/listening-edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> edit(@RequestParam Long id,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) String audioUrl,
                                     @RequestParam(required = false) String transcript,
                                     @RequestParam(required = false) String translation,
                                     @RequestParam(required = false) Integer difficulty,
                                     @RequestParam(required = false) Integer duration,
                                     @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        Listening listening = new Listening();
        listening.setId(id);
        
        if (title != null) {
            listening.setTitle(title);
        }
        if (description != null) {
            listening.setDescription(description);
        }
        if (audioUrl != null) {
            listening.setAudioUrl(audioUrl);
        }
        if (transcript != null) {
            listening.setTranscript(transcript);
        }
        if (translation != null) {
            listening.setTranslation(translation);
        }
        if (difficulty != null) {
            listening.setDifficulty(difficulty);
        }
        if (duration != null) {
            listening.setDuration(duration);
        }
        if (status != null) {
            listening.setStatus(status);
        }
        
        boolean success = listeningService.updateListening(listening);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = listeningService.deleteListening(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
