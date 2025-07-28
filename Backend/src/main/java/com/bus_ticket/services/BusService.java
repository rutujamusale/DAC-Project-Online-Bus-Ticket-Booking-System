package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusResponseDTO;
import com.bus_ticket.dto.Bus.NewBusDTO;
import com.bus_ticket.dto.Bus.UpdateBusDTO;
import com.bus_ticket.entities.Bus;

public interface BusService {
    Bus createBus(NewBusDTO newBus);
    List<BusResponseDTO> getAllBuses();
    BusResponseDTO getBusById(Long id);
    ApiResponse updateBus(Long id, UpdateBusDTO updateBus);
    ApiResponse deleteBus(Long id);
}