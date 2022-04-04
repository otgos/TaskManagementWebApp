package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MailRepository extends JpaRepository<Mail, Long> {
}
