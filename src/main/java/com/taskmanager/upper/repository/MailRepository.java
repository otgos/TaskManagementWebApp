package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MailRepository extends JpaRepository<Mail, Long> {
    @Query(
            value = "SELECT * FROM mail m " +
                    " WHERE m.receiver_id=? ORDER BY m.send_date DESC",
            nativeQuery = true)
    List<Mail> findAllByReceiver(Long id);
}
