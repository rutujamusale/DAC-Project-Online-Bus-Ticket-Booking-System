package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction_bookings")
@Getter
@Setter
@NoArgsConstructor
public class TransactionBooking extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, COMPLETED, FAILED
} 