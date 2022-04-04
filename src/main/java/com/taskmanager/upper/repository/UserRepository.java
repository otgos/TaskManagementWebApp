package com.taskmanager.upper.repository;

import com.taskmanager.upper.entity.AuthUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<AuthUsers, Long> {


    AuthUsers findByEmail(String email);

    @Query(
            value = "SELECT u.* FROM users u JOIN users_role ur ON u.id=ur.auth_users_id JOIN roles r ON ur.role_id = r.id WHERE role_id = 1",
            nativeQuery = true)
    List<AuthUsers> findAllByRole();

    List<AuthUsers> findByFullNameContainingIgnoreCase (String keyword);
}
