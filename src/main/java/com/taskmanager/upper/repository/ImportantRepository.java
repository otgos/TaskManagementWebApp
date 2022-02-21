package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Important;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportantRepository extends JpaRepository<Important, Long> {
}
