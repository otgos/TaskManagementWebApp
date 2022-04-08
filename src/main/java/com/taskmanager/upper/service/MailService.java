package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Mail;
import com.taskmanager.upper.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    public Mail addMail(Mail mail){
        mailRepository.save(mail);
        return mail;
    }
    public List<Mail> getAllMailList(Long id){
        return mailRepository.findAllByReceiver(id);
    }
    public Mail getMailById(Long id){
        return mailRepository.getById(id);
    }
}
