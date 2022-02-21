package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.AuthRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<AuthRoles, Long> {
}
