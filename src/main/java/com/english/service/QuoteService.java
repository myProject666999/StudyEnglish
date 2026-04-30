package com.english.service;

import com.english.entity.Quote;

import java.util.List;

public interface QuoteService {

    Quote findById(Long id);

    Quote getDailyQuote();

    Quote getRandomQuote();

    List<Quote> findAll();

    List<Quote> findList(String keyword);

    boolean addQuote(Quote quote);

    boolean updateQuote(Quote quote);

    boolean deleteQuote(Long id);

    boolean setDailyQuote(Long id);
}
