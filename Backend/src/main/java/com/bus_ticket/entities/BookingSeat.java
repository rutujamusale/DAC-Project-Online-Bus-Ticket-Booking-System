package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "booking_seats")
@Getter
@Setter
@NoArgsConstructor
public class BookingSeat extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    @Column(name = "passenger_name", nullable = false)
    private String passengerName;
    
    @Column(name = "passenger_age", nullable = false)
    private Integer passengerAge;
    
    @Column(name = "passenger_gender", nullable = false)
    private String passengerGender;
    
    @Column(name = "passenger_phone", nullable = false)
    private String passengerPhone;
    
    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;
    
    @Column(name = "seat_price", nullable = false)
    private Double seatPrice;
    
    public BookingSeat(Booking booking, Seat seat, String passengerName, Integer passengerAge, 
                      String passengerGender, String passengerPhone, String passengerEmail) {
        this.booking = booking;
        this.seat = seat;
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.passengerGender = passengerGender;
        this.passengerPhone = passengerPhone;
        this.passengerEmail = passengerEmail;
        this.seatPrice = seat.getPrice();
    }
}