package com.english.service.impl;

import com.english.entity.Quote;
import com.english.mapper.QuoteMapper;
import com.english.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteMapper quoteMapper;

    @Override
    public Quote findById(Long id) {
        return quoteMapper.selectById(id);
    }

    @Override
    public Quote getDailyQuote() {
        Quote daily = quoteMapper.selectDaily();
        
        if (daily == null) {
            daily = quoteMapper.selectRandom();
        }
        
        if (daily != null && daily.getDailyDate() != null) {
            if (!daily.getDailyDate().equals(LocalDate.now())) {
                daily = quoteMapper.selectRandom();
                if (daily != null) {
                    daily.setIsDaily(1);
                    daily.setDailyDate(LocalDate.now());
                    quoteMapper.update(daily);
                }
            }
        }
        
        return daily;
    }

    @Override
    public Quote getRandomQuote() {
        return quoteMapper.selectRandom();
    }

    @Override
    public List<Quote> findAll() {
        return quoteMapper.selectAll();
    }

    @Override
    public List<Quote> findList(String keyword) {
        return quoteMapper.selectList(keyword);
    }

    @Override
    public boolean addQuote(Quote quote) {
        return quoteMapper.insert(quote) > 0;
    }

    @Override
    public boolean updateQuote(Quote quote) {
        return quoteMapper.update(quote) > 0;
    }

    @Override
    public boolean deleteQuote(Long id) {
        return quoteMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDailyQuote(Long id) {
        quoteMapper.updateDailyStatus();
        
        Quote quote = quoteMapper.selectById(id);
        if (quote != null) {
            quote.setIsDaily(1);
            quote.setDailyDate(LocalDate.now());
            return quoteMapper.update(quote) > 0;
        }
        
        return false;
    }
}
