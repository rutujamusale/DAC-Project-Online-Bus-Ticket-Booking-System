package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.TransactionBooking;
import java.util.List;

@Repository
public interface TransactionBookingDao extends JpaRepository<TransactionBooking, Long> {
    List<TransactionBooking> findByTransactionId(Long transactionId);
    List<TransactionBooking> findByBookingId(Long bookingId);
} 