package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.dto.Bus.BusSearchRequest;

import java.util.List;

public interface BusService {
    
    ApiResponse addBus(BusDto busDto);
    
    List<BusDto> getAllActiveBuses();
    
    List<BusDto> getBusesByVendor(Long vendorId);
    
    BusDto getBusById(Long id);
    
    ApiResponse updateBus(Long id, BusDto busDto);
    
    ApiResponse softDeleteBus(Long id);
    
    List<BusDto> searchBuses(BusSearchRequest searchRequest);
}
