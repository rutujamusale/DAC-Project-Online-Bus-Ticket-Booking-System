package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Admin;

import java.util.Optional;

@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {
    
    Optional<Admin> findByUsername(String username);
    
    Optional<Admin> findByUsernameAndPassword(String username, String password);
}
