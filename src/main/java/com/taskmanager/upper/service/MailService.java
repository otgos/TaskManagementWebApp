package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Mail;
import com.taskmanager.upper.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    public Mail addMail(Mail mail){
        mailRepository.save(mail);
        return mail;
    }
}
