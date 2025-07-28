package com.bus_ticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
public class Route extends BaseEntity{

     // A route is created by a vendor (assumed to be a User with role=VENDOR)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    
    @Column(nullable = false)
    private String name;

    // Source city (Many routes can start from one city)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_city_id", nullable = false)
    private City sourceCity;

    // Destination city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_city_id", nullable = false)
    private City destinationCity;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

//    @OneToMany(mappedBy = "route")
//    private List<Schedule> schedules;

   
}
