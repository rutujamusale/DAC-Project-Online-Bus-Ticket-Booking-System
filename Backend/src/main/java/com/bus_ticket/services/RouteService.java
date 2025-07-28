package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.Route.NewRouteDTO;
import com.bus_ticket.dto.Route.RouteResponseDTO;

public interface RouteService {
    RouteResponseDTO createRoute(NewRouteDTO dto);
    RouteResponseDTO getRouteById(Long id);
    List<RouteResponseDTO> getAllRoutes();
    RouteResponseDTO updateRoute(Long id, NewRouteDTO dto);
    void deleteRoute(Long id);
}