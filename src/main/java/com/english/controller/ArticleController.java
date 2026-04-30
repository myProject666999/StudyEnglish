package com.english.controller;

import com.english.entity.User;
import com.english.entity.Article;
import com.english.entity.Book;
import com.english.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public String index(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false) String category,
                        Model model) {
        List<Book> books = articleService.findBookList(keyword, category);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        
        return "articles/list";
    }

    @GetMapping("/book/{bookId}")
    public String articleList(@PathVariable Long bookId, Model model) {
        Book book = articleService.findBookById(bookId);
        List<Article> articles = articleService.findByBookId(bookId);
        
        model.addAttribute("book", book);
        model.addAttribute("articles", articles);
        model.addAttribute("bookName", book != null ? book.getTitle() : "");
        
        return "articles/list";
    }

    @GetMapping("/read/{id}")
    public String readArticle(@PathVariable Long id,
                              @RequestParam(required = false) Long bookId,
                              Model model,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        
        if (bookId != null) {
            Book book = articleService.findBookById(bookId);
            model.addAttribute("book", book);
        }
        
        return "articles/list";
    }

    @PostMapping("/mark-read")
    @ResponseBody
    public Map<String, Object> markAsRead(@RequestParam Long articleId,
                                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        try {
            articleService.markAsRead(user.getId(), articleId);
            result.put("success", true);
            result.put("message", "已标记为已读");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
        }
        
        return result;
    }
}
