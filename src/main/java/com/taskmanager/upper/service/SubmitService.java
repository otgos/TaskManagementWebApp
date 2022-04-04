package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Submit;
import com.taskmanager.upper.repository.SubmitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitService {
    private final SubmitRepository submitRepository;

    public Submit addSubmit(Submit submit){
        return submitRepository.save(submit);
    }
    public List<Submit> getAllSubmitted(){return submitRepository.findAll();}
    public Submit getSubmitById(Long id){
        return submitRepository.getById(id);
    }
}
