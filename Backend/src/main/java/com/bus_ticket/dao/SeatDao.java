
package com.bus_ticket.dao;

import com.bus_ticket.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatDao extends JpaRepository<Seat, Long> {
    
    @Query("SELECT s FROM Seat s WHERE s.schedule.id = :scheduleId ORDER BY s.rowNumber, s.columnNumber")
    List<Seat> findByScheduleId(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT s FROM Seat s WHERE s.schedule.id = :scheduleId AND s.status = 'AVAILABLE' ORDER BY s.rowNumber, s.columnNumber")
    List<Seat> findAvailableSeatsByScheduleId(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT s FROM Seat s WHERE s.schedule.id = :scheduleId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByScheduleIdAndSeatNumber(@Param("scheduleId") Long scheduleId, @Param("seatNumber") String seatNumber);
    
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.schedule.id = :scheduleId AND s.status = 'AVAILABLE'")
    Long countAvailableSeatsByScheduleId(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT s FROM Seat s WHERE s.status = 'RESERVED' AND s.lockedAt < :expiryTime")
    List<Seat> findExpiredReservedSeats(@Param("expiryTime") LocalDateTime expiryTime);
    
    @Query("SELECT s FROM Seat s WHERE s.schedule.id = :scheduleId AND s.status = 'RESERVED' AND s.lockedAt < :expiryTime")
    List<Seat> findExpiredReservedSeatsByScheduleId(@Param("scheduleId") Long scheduleId, @Param("expiryTime") LocalDateTime expiryTime);
} 