package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
}
