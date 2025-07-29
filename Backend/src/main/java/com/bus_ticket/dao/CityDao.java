
package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.City;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityDao extends JpaRepository<City, Long> {
    
    Optional<City> findByCityName(String cityName);
    
    @Query("SELECT c FROM City c ORDER BY c.cityName")
    List<City> findAllCities();
    
    @Query("SELECT DISTINCT c.cityName FROM City c ORDER BY c.cityName")
    List<String> findAllCityNames();
}
