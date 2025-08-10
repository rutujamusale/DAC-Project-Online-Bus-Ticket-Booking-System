package com.bus_ticket.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionBooking> transactionBookings;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING"; // PENDING, COMPLETED, FAILED, CANCELLED

    @Column(name = "payment_method")
    private String paymentMethod; // CARD, UPI, NET_BANKING, WALLET

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "is_active")
    private boolean isActive = true;
}
