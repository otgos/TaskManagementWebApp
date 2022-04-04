package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.Submit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SubmitRepository extends JpaRepository<Submit, Long> {

}
