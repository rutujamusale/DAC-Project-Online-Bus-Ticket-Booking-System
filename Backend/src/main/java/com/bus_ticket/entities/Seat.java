
package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
public class Seat extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
    
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;
    
    @Column(name = "seat_row", nullable = false)
    private Integer rowNumber;
    
    @Column(name = "column_number", nullable = false)
    private Integer columnNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;
    
    @Column(name = "price", nullable = false)
    private Double price;
    
    @Column(name = "locked_at")
    private LocalDateTime lockedAt;
    
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingSeat> bookingSeats;
    
    public enum SeatType {
        WINDOW, AISLE, MIDDLE
    }
    
    public enum SeatStatus {
        AVAILABLE, BOOKED, RESERVED, BLOCKED
    }
    
    public Seat(Schedule schedule, String seatNumber, Integer rowNumber, Integer columnNumber, 
                SeatType seatType, Double price) {
        this.schedule = schedule;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.seatType = seatType;
        this.price = price;
    }
} 