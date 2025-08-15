package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Passenger;
import java.util.List;

@Repository
public interface PassengerDao extends JpaRepository<Passenger, Long> {
    boolean existsByUid(String uid);
    List<Passenger> findByBookingId(Long bookingId);
}
