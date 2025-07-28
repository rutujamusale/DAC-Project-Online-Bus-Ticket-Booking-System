package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bus_ticket.entities.Route;

import java.util.List;

@Repository
public interface RouteDao extends JpaRepository<Route, Long> {

    List<Route> findByIsDeletedFalse();
    
}
