package com.english.service.impl;

import com.english.entity.Article;
import com.english.entity.Book;
import com.english.entity.StudyRecord;
import com.english.mapper.ArticleMapper;
import com.english.mapper.BookMapper;
import com.english.mapper.StudyRecordMapper;
import com.english.service.ArticleService;
import com.english.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private StudyRecordMapper studyRecordMapper;

    @Autowired
    private UserProgressService userProgressService;

    @Override
    public Article findById(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public Book findBookById(Long id) {
        return bookMapper.selectById(id);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookMapper.selectAll();
    }

    @Override
    public List<Book> findBookList(String keyword, String category) {
        return bookMapper.selectList(keyword, category);
    }

    @Override
    public List<Article> findByBookId(Long bookId) {
        return articleMapper.selectByBookId(bookId);
    }

    @Override
    public List<Article> findList(String keyword, Long bookId, Integer difficulty) {
        return articleMapper.selectList(keyword, bookId, difficulty);
    }

    @Override
    public boolean addBook(Book book) {
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean updateBook(Book book) {
        return bookMapper.update(book) > 0;
    }

    @Override
    public boolean deleteBook(Long id) {
        return bookMapper.deleteById(id) > 0;
    }

    @Override
    public boolean addArticle(Article article) {
        return articleMapper.insert(article) > 0;
    }

    @Override
    public boolean updateArticle(Article article) {
        return articleMapper.update(article) > 0;
    }

    @Override
    public boolean deleteArticle(Long id) {
        return articleMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long userId, Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return;
        }
        
        StudyRecord record = new StudyRecord();
        record.setUserId(userId);
        record.setRecordType(2);
        record.setTypeId(articleId);
        record.setContent("阅读文章: " + article.getTitle());
        record.setStudyTime(0);
        studyRecordMapper.insert(record);
        
        userProgressService.updateArticleProgress(userId);
    }
}
