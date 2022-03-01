package com.taskmanager.upper.api;

import com.taskmanager.upper.entity.Announcement;
import com.taskmanager.upper.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class MainRestController {

    private final AnnouncementService announcementService;

    @GetMapping(value= "/allAnnouncements")
    public ResponseEntity<List<Announcement>> getAllAnnouncements(){

        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }
}
