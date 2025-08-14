package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;

import java.util.List;

public interface BusService {
    
    ApiResponse addBus(BusDto busDto);
    
    List<BusDto> getBusesByVendor(Long vendorId);
    
    ApiResponse deleteBus(Long id);
}
