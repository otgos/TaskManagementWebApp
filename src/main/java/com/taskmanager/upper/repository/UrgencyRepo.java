package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Urgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UrgencyRepo extends JpaRepository<Urgency, Long> {
}
