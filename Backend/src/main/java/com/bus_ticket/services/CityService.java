package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.City.CityResponseDTO;
import com.bus_ticket.dto.City.NewCityDTO;
import com.bus_ticket.entities.City;

public interface CityService {
    City createCity(NewCityDTO newCity);
    List<CityResponseDTO> getAllCity();
    CityResponseDTO getCityById(Long id);
    ApiResponse deleteCity(Long id);
}