package com.bus_ticket.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.dao.CityDao;
import com.bus_ticket.dao.RouteDao;
import com.bus_ticket.dao.VendorDao;
import com.bus_ticket.dto.Route.NewRouteDTO;
import com.bus_ticket.dto.Route.RouteResponseDTO;
import com.bus_ticket.entities.Route;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {
    @Autowired
    private final RouteDao routeDao;
    @Autowired
    private final CityDao cityDao;
    @Autowired
    private final VendorDao vendorDao;
    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public RouteResponseDTO createRoute(NewRouteDTO dto) {
        Route route = new Route();
        route.setName(dto.getName());
        route.setVendor(vendorDao.findById(dto.getVendorId()).orElseThrow());
        route.setSourceCity(cityDao.findById(dto.getSourceCityId()).orElseThrow());
        route.setDestinationCity(cityDao.findById(dto.getDestinationCityId()).orElseThrow());
        route.setDeleted(false);

        return modelMapper.map(routeDao.save(route), RouteResponseDTO.class);
    }

    @Override
    public RouteResponseDTO getRouteById(Long id) {
        Route route = routeDao.findById(id).orElseThrow();
        return modelMapper.map(route, RouteResponseDTO.class);
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeDao.findByIsDeletedFalse().stream()
                .map(route -> modelMapper.map(route, RouteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RouteResponseDTO updateRoute(Long id, NewRouteDTO dto) {
        Route route = routeDao.findById(id).orElseThrow();
        route.setName(dto.getName());
        route.setVendor(vendorDao.findById(dto.getVendorId()).orElseThrow());
        route.setSourceCity(cityDao.findById(dto.getSourceCityId()).orElseThrow());
        route.setDestinationCity(cityDao.findById(dto.getDestinationCityId()).orElseThrow());

        return modelMapper.map(routeDao.save(route), RouteResponseDTO.class);
    }

    @Override
    public void deleteRoute(Long id) {
        Route route = routeDao.findById(id).orElseThrow();
        route.setDeleted(true);
        routeDao.save(route);
    }
    
}
