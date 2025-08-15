package com.bus_ticket.dao;

import com.bus_ticket.entities.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatDao extends JpaRepository<BookingSeat, Long> {
    
    @Query("SELECT bs FROM BookingSeat bs WHERE bs.booking.id = :bookingId")
    List<BookingSeat> findByBookingId(@Param("bookingId") Long bookingId);
    
    @Query("SELECT bs FROM BookingSeat bs WHERE bs.seat.id = :seatId")
    List<BookingSeat> findBySeatId(@Param("seatId") Long seatId);
} 