package com.english.controller;

import com.english.entity.Quote;
import com.english.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping("/daily")
    @ResponseBody
    public Map<String, Object> getDailyQuote() {
        Map<String, Object> result = new HashMap<>();
        
        Quote quote = quoteService.getDailyQuote();
        if (quote != null) {
            result.put("success", true);
            result.put("content", quote.getContent());
            result.put("contentCn", quote.getContentCn());
            result.put("author", quote.getAuthor());
            result.put("source", quote.getSource());
        } else {
            result.put("success", false);
            result.put("content", "Practice makes perfect.");
            result.put("contentCn", "熟能生巧。");
            result.put("author", "佚名");
        }
        
        return result;
    }

    @GetMapping("/random")
    @ResponseBody
    public Map<String, Object> getRandomQuote() {
        Map<String, Object> result = new HashMap<>();
        
        Quote quote = quoteService.getRandomQuote();
        if (quote != null) {
            result.put("success", true);
            result.put("content", quote.getContent());
            result.put("contentCn", quote.getContentCn());
            result.put("author", quote.getAuthor());
            result.put("source", quote.getSource());
        } else {
            result.put("success", false);
        }
        
        return result;
    }
}
