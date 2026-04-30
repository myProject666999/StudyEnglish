package com.english.service;

import com.english.entity.Article;
import com.english.entity.Book;

import java.util.List;

public interface ArticleService {

    Article findById(Long id);

    Book findBookById(Long id);

    List<Book> findAllBooks();

    List<Book> findBookList(String keyword, String category);

    List<Article> findByBookId(Long bookId);

    List<Article> findList(String keyword, Long bookId, Integer difficulty);

    boolean addBook(Book book);

    boolean updateBook(Book book);

    boolean deleteBook(Long id);

    boolean addArticle(Article article);

    boolean updateArticle(Article article);

    boolean deleteArticle(Long id);

    void markAsRead(Long userId, Long articleId);
}
