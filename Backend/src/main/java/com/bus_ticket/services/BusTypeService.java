package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.BusType.BusTypeResponseDTO;
import com.bus_ticket.dto.BusType.NewBusTypeDTO;
import com.bus_ticket.entities.BusType;

public interface BusTypeService {
    BusType createBusType(NewBusTypeDTO newBusType);
    List<BusTypeResponseDTO> getAllBusTypes();
    BusTypeResponseDTO getBusTypeById(Long id);
    ApiResponse deleteBusType(Long id);
}