package com.bus_ticket.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.BusDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusResponseDTO;
import com.bus_ticket.dto.Bus.NewBusDTO;
import com.bus_ticket.dto.Bus.UpdateBusDTO;
import com.bus_ticket.entities.Bus;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BusServiceImpl implements BusService{
    @Autowired
    private BusDao BusDao;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Bus createBus(NewBusDTO newBus) {
        Bus entity = modelMapper.map(newBus,Bus.class);
        return BusDao.save(entity);
    }

    @Override
    public List<BusResponseDTO> getAllBuses() {
        return BusDao.findByIsDeletedFalse() //List<entity>
				.stream() //Stream<entity>
				.map(Bus -> 
				modelMapper.map(Bus, BusResponseDTO.class)) //Stream<dto>
				.toList();
    }

    @Override
    public BusResponseDTO getBusById(Long id) {
        Bus entity = BusDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid Bus ID !!!!!"));
        return modelMapper.map(entity, BusResponseDTO.class);
    }

    @Override
    public ApiResponse updateBus(Long id, UpdateBusDTO updateBus) {
        Bus entity = BusDao.findById(id)
            .orElseThrow(() ->
            new ResourceNotFoundException("Invalid Bus ID : Update failed"));
            
        modelMapper.map(updateBus, entity);  

        return new ApiResponse("Bus updated!!");
    }

    @Override
    public ApiResponse deleteBus(Long id) {
        Bus entity = BusDao.findById(id)
            .orElseThrow(() ->
            new ResourceNotFoundException("Invalid Bus ID : Update failed"));

        entity.setDeleted(true);
        return new ApiResponse("Bus deleted!!");
    }
    
}
