package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ResponseRepo extends JpaRepository<Response, Long> {
}
