package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.AuthUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<AuthUsers, Long> {

    AuthUsers findByEmail(String email);
}
