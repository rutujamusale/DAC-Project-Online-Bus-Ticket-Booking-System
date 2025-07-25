package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Bus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusDao extends JpaRepository<Bus, Long> {
    
    Optional<Bus> findByBusNumber(String busNumber);
    
    @Query("SELECT b FROM Bus b WHERE b.vendor.id = :vendorId AND b.isDeleted = false")
    List<Bus> findByVendorId(@Param("vendorId") Long vendorId);
    
    @Query("SELECT b FROM Bus b WHERE b.source = :source AND b.destination = :destination AND b.isDeleted = false")
    List<Bus> findBySourceAndDestination(@Param("source") String source, @Param("destination") String destination);
    
    @Query("SELECT b FROM Bus b WHERE b.isDeleted = false")
    List<Bus> findAllActiveBuses();
}
