package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Vendor;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorDao extends JpaRepository<Vendor, Long> {
    
    Optional<Vendor> findByEmail(String email);
    
    Optional<Vendor> findByEmailAndPassword(String email, String password);
    
    @Query("SELECT v FROM Vendor v WHERE v.isDeleted = false")
    List<Vendor> findAllActiveVendors();
    
    @Query("SELECT COUNT(v) FROM Vendor v WHERE v.isDeleted = false")
    Long countActiveVendors();
}
