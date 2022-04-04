package com.taskmanager.upper.service;

import com.taskmanager.upper.dto.CommentDto;
import com.taskmanager.upper.entity.Announcement;
import com.taskmanager.upper.entity.Comment;
import com.taskmanager.upper.mapper.CommentMapper;
import com.taskmanager.upper.repository.AnnouncementRepo;
import com.taskmanager.upper.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepo announcementRepo;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    public Announcement addAnnouncement(Announcement announcement){
        return announcementRepo.save(announcement);
    }
    public List<Announcement> getAllAnnouncements(){
        return announcementRepo.findAll();
    }

    public Announcement getAnnouncement(Long id){
        return announcementRepo.findById(id).orElse(null);
    }

    public Comment addComment(Comment comment){
       return commentRepo.save(comment);
    }

    public List<CommentDto> findAllCommentDto(Long annId){
        ArrayList<CommentDto> commentDtoList = new ArrayList<>();
        Announcement announcement = getAnnouncement(annId);
        if (announcement!=null){
            List<Comment> commentList=commentRepo.findAllByAnnouncementOrderByPostDateDesc(announcement);
            if(!CollectionUtils.isEmpty(commentList)){
               return commentMapper.toCommentDtoList(commentList);
            }
        }
        return null;
    }

    public Comment getComment(Long id){
        return commentRepo.findById(id).orElse(null);
    }

    public void deleteComment(Long id){
        commentRepo.deleteById(id);
    }
}

