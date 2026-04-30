package com.english.service.impl;

import com.english.entity.Listening;
import com.english.entity.StudyRecord;
import com.english.mapper.ListeningMapper;
import com.english.mapper.StudyRecordMapper;
import com.english.service.ListeningService;
import com.english.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListeningServiceImpl implements ListeningService {

    @Autowired
    private ListeningMapper listeningMapper;

    @Autowired
    private StudyRecordMapper studyRecordMapper;

    @Autowired
    private UserProgressService userProgressService;

    @Override
    public Listening findById(Long id) {
        return listeningMapper.selectById(id);
    }

    @Override
    public List<Listening> findAll() {
        return listeningMapper.selectAll();
    }

    @Override
    public List<Listening> findList(String keyword, Integer difficulty) {
        return listeningMapper.selectList(keyword, difficulty);
    }

    @Override
    public boolean addListening(Listening listening) {
        return listeningMapper.insert(listening) > 0;
    }

    @Override
    public boolean updateListening(Listening listening) {
        return listeningMapper.update(listening) > 0;
    }

    @Override
    public boolean deleteListening(Long id) {
        return listeningMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsCompleted(Long userId, Long listeningId) {
        Listening listening = listeningMapper.selectById(listeningId);
        if (listening == null) {
            return;
        }
        
        StudyRecord record = new StudyRecord();
        record.setUserId(userId);
        record.setRecordType(3);
        record.setTypeId(listeningId);
        record.setContent("听力练习: " + listening.getTitle());
        record.setStudyTime(0);
        studyRecordMapper.insert(record);
        
        userProgressService.updateListeningProgress(userId);
    }
}
