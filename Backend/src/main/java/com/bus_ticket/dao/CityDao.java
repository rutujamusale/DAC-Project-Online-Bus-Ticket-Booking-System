package com.bus_ticket.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.City;


@Repository
public interface CityDao extends JpaRepository<City, Long> {

    List<City> findByIsDeletedFalse();
    
    Optional<City> findByIdAndIsDeletedFalse(Long id);
    
}
