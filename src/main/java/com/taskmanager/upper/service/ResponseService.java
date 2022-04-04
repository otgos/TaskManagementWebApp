package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Response;
import com.taskmanager.upper.repository.ResponseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepo responseRepo;

    public List<Response> getAllResponse(){
        return responseRepo.findAll();
    }

    public Response addResponse(Response response){
        responseRepo.save(response);
        return response;
    }
}
