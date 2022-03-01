package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.Announcement;
import com.taskmanager.upper.repository.AnnouncementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepo announcementRepo;

    public Announcement addAnnouncement(Announcement announcement){
        return announcementRepo.save(announcement);
    }
    public List<Announcement> getAllAnnouncements(){
        return announcementRepo.findAll();
    }

    public Announcement getAnnouncement(Long id){
        return announcementRepo.findById(id).orElse(null);
    }
}
