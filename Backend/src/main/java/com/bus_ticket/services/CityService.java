
package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.City.CityDto;

import java.util.List;

public interface CityService {
    ApiResponse addCity(CityDto cityDto);
    List<CityDto> getAllCities();
    List<String> getAllCityNames();
    CityDto getCityById(Long id);
    ApiResponse updateCity(Long id, CityDto cityDto);
    ApiResponse deleteCity(Long id);
}
