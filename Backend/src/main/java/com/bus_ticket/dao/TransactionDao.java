package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Transaction;
import java.util.List;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByPaymentStatus(String paymentStatus);
    List<Transaction> findByUserIdAndPaymentStatus(Long userId, String paymentStatus);
}
