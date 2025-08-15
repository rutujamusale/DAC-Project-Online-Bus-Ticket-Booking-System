package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bus_ticket.entities.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleDao extends JpaRepository<Schedule, Long> {
    
    @Query("SELECT s FROM Schedule s WHERE s.bus.id = :busId AND s.isActive = true")
    List<Schedule> findByBusId(@Param("busId") Long busId);
    
    @Query("SELECT s FROM Schedule s WHERE s.bus.id = :busId")
    List<Schedule> findAllByBusId(@Param("busId") Long busId);
    
    @Query("SELECT s FROM Schedule s WHERE s.bus.vendor.id = :vendorId AND s.isActive = true")
    List<Schedule> findByVendorId(@Param("vendorId") Long vendorId);
    
    @Query("SELECT s FROM Schedule s WHERE s.source = :source AND s.destination = :destination AND s.scheduleDate = :date AND s.isActive = true")
    List<Schedule> findScheduledBuses(@Param("source") String source, @Param("destination") String destination, @Param("date") LocalDate date);
    
    @Query("SELECT s FROM Schedule s WHERE s.bus.id = :busId AND s.scheduleDate = :date AND s.isActive = true")
    Optional<Schedule> findByBusIdAndDate(@Param("busId") Long busId, @Param("date") LocalDate date);
}
