package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Reject;
import com.taskmanager.upper.entity.Response;
import com.taskmanager.upper.repository.RejectRepo;
import com.taskmanager.upper.repository.ResponseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RejectService {
    private final RejectRepo rejectRepo;

    public List<Reject> getAllResponse(){
        return rejectRepo.findAll();
    }

    public Reject addReject(Reject reject){
        rejectRepo.save(reject);
        return reject;
    }
}
