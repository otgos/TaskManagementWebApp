package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<Tasks, Long> {

    @Query(
            value = "SELECT * FROM tasks t WHERE t.urgency_id = 1",
            nativeQuery = true)
    List<Tasks> findAllUrgentTasks();

    @Query(
            value = "SELECT * FROM tasks t WHERE t.urgency_id = 2",
            nativeQuery = true)
    List<Tasks> findAllNotUrgentTasks();

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 1",
            nativeQuery = true)
    List<Tasks> findAllImportantTasks();

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 2",
            nativeQuery = true)
    List<Tasks> findAllNotImportantTasks();

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 1 AND t.urgency_id = 1",
            nativeQuery = true)
    List<Tasks> findAllNotImportantAndUrgentTasks();

    List<Tasks> findAllByOrderByDuedateDesc();

}
