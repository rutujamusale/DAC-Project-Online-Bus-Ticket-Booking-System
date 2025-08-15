package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findActiveByEmail(String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = false")
    Long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate AND u.isDeleted = false")
    Long countUsersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
