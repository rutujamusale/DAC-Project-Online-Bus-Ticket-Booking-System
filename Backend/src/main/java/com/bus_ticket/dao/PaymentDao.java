package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Payment;
import com.bus_ticket.entities.Transaction;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Long> {
    boolean existsByTransaction(Transaction transaction);

}
