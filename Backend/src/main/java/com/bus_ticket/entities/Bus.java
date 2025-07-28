package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bus extends BaseEntity {
    
    @Column(name = "bus_number", unique = true, nullable = false)
    private String busNumber;
    
    @Column(name = "bus_name", nullable = false)
    private String busName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private BusType busType;
    
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    private Integer seatRows;
    
    private Integer seatCols;
    
    // @OneToMany(mappedBy = "bus")
    // private List<Schedule> schedules;
}
