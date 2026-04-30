package com.english.controller.admin;

import com.english.entity.Article;
import com.english.entity.Book;
import com.english.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/articles")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/books")
    public String bookList(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String category,
                          Model model) {
        List<Book> books = articleService.findBookList(keyword, category);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        
        return "admin/book-list";
    }

    @GetMapping("/books/add")
    public String addBookPage() {
        return "admin/book-add";
    }

    @PostMapping("/books/add")
    @ResponseBody
    public Map<String, Object> addBook(@RequestParam String title,
                                        @RequestParam(required = false) String author,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) String category,
                                        @RequestParam(defaultValue = "1") Integer difficulty) {
        Map<String, Object> result = new HashMap<>();
        
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setCategory(category);
        book.setDifficulty(difficulty);
        book.setStatus(1);
        
        boolean success = articleService.addBook(book);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/books/edit/{id}")
    public String editBookPage(@PathVariable Long id, Model model) {
        Book book = articleService.findBookById(id);
        model.addAttribute("book", book);
        return "admin/book-edit";
    }

    @PostMapping("/books/edit")
    @ResponseBody
    public Map<String, Object> editBook(@RequestParam Long id,
                                         @RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) Integer difficulty,
                                         @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        Book book = new Book();
        book.setId(id);
        
        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (description != null) {
            book.setDescription(description);
        }
        if (category != null) {
            book.setCategory(category);
        }
        if (difficulty != null) {
            book.setDifficulty(difficulty);
        }
        if (status != null) {
            book.setStatus(status);
        }
        
        boolean success = articleService.updateBook(book);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/books/delete")
    @ResponseBody
    public Map<String, Object> deleteBook(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = articleService.deleteBook(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("")
    public String articleList(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Long bookId,
                              @RequestParam(required = false) Integer difficulty,
                              Model model) {
        List<Book> books = articleService.findAllBooks();
        List<Article> articles = articleService.findList(keyword, bookId, difficulty);
        
        model.addAttribute("articles", articles);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("bookId", bookId);
        model.addAttribute("difficulty", difficulty);
        
        return "admin/article-list";
    }

    @GetMapping("/add")
    public String addArticlePage(Model model) {
        List<Book> books = articleService.findAllBooks();
        model.addAttribute("books", books);
        return "admin/article-add";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addArticle(@RequestParam(required = false) Long bookId,
                                           @RequestParam String title,
                                           @RequestParam String content,
                                           @RequestParam(required = false) String translation,
                                           @RequestParam(defaultValue = "1") Integer difficulty,
                                           @RequestParam(required = false) String audioUrl) {
        Map<String, Object> result = new HashMap<>();
        
        Article article = new Article();
        article.setBookId(bookId);
        article.setTitle(title);
        article.setContent(content);
        article.setTranslation(translation);
        article.setDifficulty(difficulty);
        article.setAudioUrl(audioUrl);
        article.setWordCount(content.length());
        article.setStatus(1);
        
        boolean success = articleService.addArticle(article);
        
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/edit/{id}")
    public String editArticlePage(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        List<Book> books = articleService.findAllBooks();
        
        model.addAttribute("article", article);
        model.addAttribute("books", books);
        return "admin/article-edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> editArticle(@RequestParam Long id,
                                            @RequestParam(required = false) Long bookId,
                                            @RequestParam(required = false) String title,
                                            @RequestParam(required = false) String content,
                                            @RequestParam(required = false) String translation,
                                            @RequestParam(required = false) Integer difficulty,
                                            @RequestParam(required = false) String audioUrl,
                                            @RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        Article article = new Article();
        article.setId(id);
        
        if (bookId != null) {
            article.setBookId(bookId);
        }
        if (title != null) {
            article.setTitle(title);
        }
        if (content != null) {
            article.setContent(content);
            article.setWordCount(content.length());
        }
        if (translation != null) {
            article.setTranslation(translation);
        }
        if (difficulty != null) {
            article.setDifficulty(difficulty);
        }
        if (audioUrl != null) {
            article.setAudioUrl(audioUrl);
        }
        if (status != null) {
            article.setStatus(status);
        }
        
        boolean success = articleService.updateArticle(article);
        
        result.put("success", success);
        result.put("message", success ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteArticle(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = articleService.deleteArticle(id);
        
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
