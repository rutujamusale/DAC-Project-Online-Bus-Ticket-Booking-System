package com.cdacproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cdacproject.entities.User;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = false")
    Long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    Long countUsersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
