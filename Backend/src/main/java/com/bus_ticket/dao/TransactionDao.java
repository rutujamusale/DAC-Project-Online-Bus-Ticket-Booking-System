package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Transaction;


@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {
    
    
}
