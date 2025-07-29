
package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
public class City extends BaseEntity {
    
    @Column(name = "city_name", unique = true, nullable = false)
    private String cityName;
    
    public City(String cityName) {
        this.cityName = cityName;
    }
}
