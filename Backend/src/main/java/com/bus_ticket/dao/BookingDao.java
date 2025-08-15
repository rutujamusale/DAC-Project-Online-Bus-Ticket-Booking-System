package com.bus_ticket.dao;

import com.bus_ticket.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDao extends JpaRepository<Booking, Long> {
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.bookingDate DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = 'PENDING' ORDER BY b.bookingDate DESC")
    List<Booking> findPendingBookingsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.schedule.id = :scheduleId AND b.status = 'PENDING'")
    List<Booking> findPendingBookingsByScheduleId(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' ORDER BY b.bookingDate ASC")
    List<Booking> findAllPendingBookings();
}
