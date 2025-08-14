package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bus_ticket.entities.Bus;
import java.util.List;
import java.util.Optional;

public interface BusDao extends JpaRepository<Bus, Long> {
    
    @Query("SELECT b FROM Bus b WHERE b.busNumber = :busNumber")
    Optional<Bus> findByBusNumber(String busNumber);
    
    @Query("SELECT b FROM Bus b WHERE b.vendor.id = :vendorId")
    List<Bus> findByVendorId(@Param("vendorId") Long vendorId);
    
    @Query("SELECT b FROM Bus b")
    List<Bus> findAllBuses();
}
