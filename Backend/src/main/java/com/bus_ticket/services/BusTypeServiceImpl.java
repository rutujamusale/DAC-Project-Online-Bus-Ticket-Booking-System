package com.bus_ticket.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.BusTypeDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.BusType.BusTypeResponseDTO;
import com.bus_ticket.dto.BusType.NewBusTypeDTO;
import com.bus_ticket.entities.BusType;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BusTypeServiceImpl implements BusTypeService{
    @Autowired
    private BusTypeDao BusTypeDao;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public BusType createBusType(NewBusTypeDTO newBusType) {
        BusType entity = modelMapper.map(newBusType,BusType.class);
        return BusTypeDao.save(entity);
    }

    @Override
    public List<BusTypeResponseDTO> getAllBusTypes() {
        return BusTypeDao.findAll() //List<entity>
				.stream() //Stream<entity>
				.map(BusType -> 
				modelMapper.map(BusType, BusTypeResponseDTO.class)) //Stream<dto>
				.toList();
    }

    @Override
    public BusTypeResponseDTO getBusTypeById(Long id) {
        BusType entity = BusTypeDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid BusType ID !!!!!"));
        return modelMapper.map(entity, BusTypeResponseDTO.class);
    }

    @Override
    public ApiResponse deleteBusType(Long id) {
        BusType entity = BusTypeDao.findById(id)
            .orElseThrow(() ->
            new ResourceNotFoundException("Invalid BusType ID : Update failed"));

        entity.setDeleted(true);
        return new ApiResponse("BusType deleted!!");
    }
    
}
