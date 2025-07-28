package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Booking;

import java.util.List;

@Repository
public interface BookingDao extends JpaRepository<Booking, Long> {
    
    List<Booking> findByActiveBookingTrue();
    
}
