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
            value = "SELECT * FROM tasks t WHERE t.urgency_id = 1 AND t.user_id = ? AND t.response_id IS NULL ",
            nativeQuery = true)
    List<Tasks> findAllUrgentTasks(Long id);

    @Query(
            value = "SELECT * FROM tasks t WHERE t.urgency_id = 2 AND t.user_id = ? ",
            nativeQuery = true)
    List<Tasks> findAllNotUrgentTasks(Long id);

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 1 AND t.user_id = ? AND t.response_id IS NULL",
            nativeQuery = true)
    List<Tasks> findAllImportantTasks(Long id);

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 2 AND t.user_id = ? AND t.response_id IS NULL ",
            nativeQuery = true)
    List<Tasks> findAllNotImportantTasks(Long id);

    @Query(
            value = "SELECT * FROM tasks t WHERE t.important_id = 1 AND t.urgency_id = 1 AND t.user_id = ? AND t.response_id IS NULL ",
            nativeQuery = true)
    List<Tasks> findAllNotImportantAndUrgentTasks(Long id);


    List<Tasks> findAllByOrderByDuedateDesc();

    @Query(
            value = "SELECT * FROM tasks t WHERE t.user_id = ? AND t.response_id IS NULL",
            nativeQuery = true)
    List<Tasks> findAllByUserId(Long id);

    @Query(
            value = "SELECT * FROM tasks t " +
                    " JOIN response r ON t.response_id = r.id" +
                    " WHERE t.user_id = ? AND r.acceptance = 'Planned' AND t.submit_id IS NULL",
            nativeQuery = true)
    List<Tasks> findAllByUserIdAndResponseAccepted(Long id);

    @Query(
            value = "SELECT * FROM tasks t " +
                    " WHERE t.submit_id IS NOT NULL",
            nativeQuery = true)
    List<Tasks> findAllSubmittedTasks();

    @Query(
            value = "SELECT * FROM tasks t " +
                    " WHERE t.response_id IS NOT NULL AND t.submit_id IS NULL AND t.rejected_id IS NULL",
            nativeQuery = true)
    List<Tasks> findAllRespndedTasks();

    @Query(
            value = "SELECT * FROM tasks t " +
                    " WHERE t.response_id IS NULL AND user_id IS NOT NULL",
            nativeQuery = true)
    List<Tasks> findAllPendingTasks();

    @Query(
            value = "SELECT * FROM tasks t " +
                    " WHERE t.rejected_id IS NOT NULL",
            nativeQuery = true)
    List<Tasks> findAllRejectedTasks();
    @Query(
            value = "SELECT * FROM tasks t " +
                    " WHERE t.user_id IS NULL",
            nativeQuery = true)
    List<Tasks> findAllUnassignedTasks();

}
