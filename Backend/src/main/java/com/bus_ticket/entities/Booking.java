
package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSeat> bookingSeats;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers;
    
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING"; // PENDING, COMPLETED, FAILED
    
    @Column(name = "active_booking", nullable = false)
    private Boolean activeBooking = true;
    
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED, PAYMENT_PENDING, PAYMENT_FAILED
    }
    
    public Booking(User user, Schedule schedule, Double totalAmount) {
        this.user = user;
        this.schedule = schedule;
        this.totalAmount = totalAmount;
        this.bookingDate = LocalDateTime.now();
        this.activeBooking = true;
    }
}

