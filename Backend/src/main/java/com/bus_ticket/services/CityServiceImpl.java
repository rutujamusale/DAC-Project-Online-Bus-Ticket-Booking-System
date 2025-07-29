
package com.bus_ticket.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus_ticket.dao.CityDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.City.CityDto;
import com.bus_ticket.entities.City;
import com.bus_ticket.custom_exceptions.ApiException;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CityServiceImpl implements CityService {
    
    @Autowired
    private CityDao cityDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public ApiResponse addCity(CityDto cityDto) {
        if (cityDao.findByCityName(cityDto.getCityName()).isPresent()) {
            throw new ApiException("City with name " + cityDto.getCityName() + " already exists");
        }
        
        City city = modelMapper.map(cityDto, City.class);
        cityDao.save(city);
        return new ApiResponse("City added successfully");
    }
    
    @Override
    public List<CityDto> getAllCities() {
        return cityDao.findAllCities()
                .stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllCityNames() {
        return cityDao.findAllCityNames();
    }
    
    @Override
    public CityDto getCityById(Long id) {
        City city = cityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        
        return modelMapper.map(city, CityDto.class);
    }
    
    @Override
    public ApiResponse updateCity(Long id, CityDto cityDto) {
        City city = cityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        
        // Check if city name is being changed and if it already exists
        if (!city.getCityName().equals(cityDto.getCityName())) {
            if (cityDao.findByCityName(cityDto.getCityName()).isPresent()) {
                throw new ApiException("City with name " + cityDto.getCityName() + " already exists");
            }
        }
        
        city.setCityName(cityDto.getCityName());
        
        cityDao.save(city);
        return new ApiResponse("City updated successfully");
    }
    
    @Override
    public ApiResponse deleteCity(Long id) {
        City city = cityDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        
        cityDao.delete(city);
        return new ApiResponse("City deleted successfully");
    }
}
