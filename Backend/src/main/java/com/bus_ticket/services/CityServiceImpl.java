package com.bus_ticket.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.CityDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.City.CityResponseDTO;
import com.bus_ticket.dto.City.NewCityDTO;
import com.bus_ticket.entities.City;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    @Autowired
    private CityDao CityDao;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public City createCity(NewCityDTO newCity) {
        City entity = modelMapper.map(newCity,City.class);
        return CityDao.save(entity);
    }

    @Override
    public List<CityResponseDTO> getAllCity() {
        return CityDao.findAll() //List<entity>
				.stream() //Stream<entity>
				.map(City -> 
				modelMapper.map(City, CityResponseDTO.class)) //Stream<dto>
				.toList();
    }

    @Override
    public CityResponseDTO getCityById(Long id) {
        City entity = CityDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid City ID !!!!!"));
        return modelMapper.map(entity, CityResponseDTO.class);
    }

    @Override
    public ApiResponse deleteCity(Long id) {
        City entity = CityDao.findById(id)
            .orElseThrow(() ->
            new ResourceNotFoundException("Invalid City ID : Update failed"));

        entity.setDeleted(true);
        return new ApiResponse("City deleted!!");
    }
    
}
