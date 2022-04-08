package com.taskmanager.upper.controller;

import com.taskmanager.upper.auth.AuthUserConfig;
import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class MailController extends AuthUserConfig {
    @Autowired
    MailService mailService;

    @PostMapping("/mail/composeMail")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> composeMail(@RequestParam(name="subject") String subject,
                                              @RequestParam(name="senderId") AuthUsers senderId,
                                              @RequestParam(name="ids") AuthUsers receiverId,
                                              @RequestParam(name="mail") String body) {
        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.setMail(body);
        mail.setSendDate(new Date());
        mail.setReceiver(senderId);
        mail.setSender(receiverId);
        Mail newMail = mailService.addMail(mail);
        if (newMail != null) {
            return new ResponseEntity<>("Message Sent", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message Not Sent", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/mail/mailList")
    @PreAuthorize("isAuthenticated()")
    public String userIndex(Model model) {
        List<Mail> mails = mailService.getAllMailList(getCurrentUser().getId());
        model.addAttribute("mails", mails);
        model.addAttribute("currentUser", getCurrentUser());
        return "/email/emailList";
    }

    @GetMapping("/mail/readMail/{id}")
    @PreAuthorize("isAuthenticated()")
    public String readAnnouncement(Model model, @PathVariable(name = "id") Long id) {
        Mail mails = mailService.getMailById(id);
        model.addAttribute("mails", mails);
        model.addAttribute("currentUser", getCurrentUser());
        return "email/readMail";
    }
}
