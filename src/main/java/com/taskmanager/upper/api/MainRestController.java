package com.taskmanager.upper.api;

import com.taskmanager.upper.auth.AuthUserConfig;
import com.taskmanager.upper.dto.CommentDto;
import com.taskmanager.upper.entity.*;
import com.taskmanager.upper.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class MainRestController extends AuthUserConfig {

    private final AnnouncementService announcementService;
    private final TaskService taskService;
    private final ResponseService responseService;
    private final RejectService rejectService;
    private final MailService mailService;

    @GetMapping(value= "/allAnnouncements")
    public ResponseEntity<List<Announcement>> getAllAnnouncements(){

        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }
    @PostMapping("/announcement/addComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addComment(@RequestParam(name="comment") String comment,
                                             @RequestParam(name="annId") Long id){

        Announcement announcement = announcementService.getAnnouncement(id);
        if(announcement!=null){
            Comment comm= new Comment();
            comm.setAnnouncement(announcement);
            comm.setPostDate(new Date());
            comm.setAuthor(getCurrentUser());
            comm.setComment(comment);
            Comment newComm = announcementService.addComment(comm);
            if(newComm!=null){
                return new ResponseEntity<>("ADDED", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("News Not Found", HttpStatus.NOT_FOUND);

    }

    @GetMapping ("/announcement/allComments")
    public ResponseEntity<List<CommentDto >> getAllComments(@RequestParam(name="annId") Long annId){
        return new ResponseEntity<>(announcementService.findAllCommentDto(annId), HttpStatus.OK);
    }
    @PostMapping("/announcement/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteComment(@RequestParam("id") Long id){
        AuthUsers authUser = getCurrentUser();
        Comment comment = announcementService.getComment(id);
        if(comment!=null && comment.getAuthor().getId()==authUser.getId()){
            announcementService.deleteComment(id);
            return ResponseEntity.ok("Deleted");
        }return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/response/addResponse")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addResponse(@RequestParam(name="comments") String comment,
                                              @RequestParam(name="taskId") Long id,
                                              @RequestParam(name="plannedDate") Date plannedDate,
                                              @RequestParam(name="acceptance") String acceptance){
        Tasks task = taskService.findById(id);
        if(task!=null){
            Response resp = new Response();
            resp.setTasks(task);
            resp.setComment(comment);
            resp.setPlannedDate(plannedDate);
            resp.setAuthor(getCurrentUser());
            resp.setAcceptance(acceptance);
            Response newResp = responseService.addResponse(resp);
            if(newResp!=null){
                task.setResponse(newResp);
                taskService.addTask(task);
                return new ResponseEntity<>("Response Added", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Task Not Found", HttpStatus.NOT_FOUND);

    }

    @PostMapping("/response/addReject")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addReject(@RequestParam(name="comments") String comment,
                                              @RequestParam(name="taskId") Long id,
                                              @RequestParam(name="acceptance") String acceptance){
        Tasks task = taskService.findById(id);
        if(task!=null){
            Reject rej = new Reject();
            rej.setTasks(task);
            rej.setComment(comment);
            rej.setRejectDate(new Date());
            rej.setAuthor(getCurrentUser());
            rej.setRejected(acceptance);
            Reject newRej = rejectService.addReject(rej);
            if(newRej!=null){
                task.setRejected(newRej);
                taskService.addTask(task);
                return new ResponseEntity<>("Response Added", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Task Not Found", HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/mail/composeMail")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> composeMail(@RequestParam(name="subject") String subject,
                                             @RequestParam(name="senderId") AuthUsers senderId,
                                             @RequestParam(name="ids") AuthUsers receiverId,
                                             @RequestParam(name="mail") String body){

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.setMail(body);
        mail.setSendDate(new Date());
        mail.setReceiver(senderId);
        mail.setSender(receiverId);
        Mail newMail = mailService.addMail(mail);
        if(newMail!=null){
            return new ResponseEntity<>("Message Sent Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message Failed", HttpStatus.NOT_FOUND);

    }

}
