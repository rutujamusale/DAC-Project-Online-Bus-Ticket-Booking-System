package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
public class Bus extends BaseEntity {
    
    @Column(name = "bus_number", unique = true, nullable = false)
    private String busNumber;
    
    @Column(name = "bus_name", nullable = false)
    private String busName;
    
    @Column(name = "source", nullable = false)
    private String source;
    
    @Column(name = "destination", nullable = false)
    private String destination;
    
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;
    
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @Column(name = "bus_type")
    private String busType;
    
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
}
