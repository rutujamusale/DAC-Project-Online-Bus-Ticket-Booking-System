package com.bus_ticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
    
    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    
    @NotNull(message = "Seat column must be specified")
    @Min(value = 1, message = "Seat column must be at least 1")
    @Column(name = "seat_col", nullable = false)
    private Integer seatCol;
    
    @NotNull(message = "Seat row must be specified")
    @Min(value = 1, message = "Seat row must be at least 1")
    @Column(name = "seat_row", nullable = false)
    private Integer seatRow;

    @Column(name = "active_booking", nullable = false)
    private Boolean activeBooking = true;
}
