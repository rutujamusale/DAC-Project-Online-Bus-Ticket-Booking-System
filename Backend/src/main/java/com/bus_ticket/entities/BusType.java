package com.bus_ticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bus_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusType extends BaseEntity{
    
    @Column(name = "type")
    private String type;     // e.g. "AC Sleeper"

    @Column(name = "is_deleted")
    private Boolean deleted = false;

//    @OneToMany(mappedBy = "busType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Bus> buses;

}
