package com.english.service;

import com.english.entity.Listening;

import java.util.List;

public interface ListeningService {

    Listening findById(Long id);

    List<Listening> findAll();

    List<Listening> findList(String keyword, Integer difficulty);

    boolean addListening(Listening listening);

    boolean updateListening(Listening listening);

    boolean deleteListening(Long id);

    void markAsCompleted(Long userId, Long listeningId);
}
