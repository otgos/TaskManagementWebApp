package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Announcement;
import com.taskmanager.upper.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAnnouncementOrderByPostDateDesc(Announcement announcement);
}
