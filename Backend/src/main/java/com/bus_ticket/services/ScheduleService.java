package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Schedule.ScheduleDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    
    ApiResponse addSchedule(ScheduleDto scheduleDto);
    
    List<ScheduleDto> getSchedulesByBusId(Long busId);
    
    List<ScheduleDto> getSchedulesByVendorId(Long vendorId);
    
    List<ScheduleDto> searchScheduledBuses(String source, String destination, LocalDate date);
    
    ScheduleDto getScheduleById(Long id);
    
    ApiResponse updateSchedule(Long id, ScheduleDto scheduleDto);
    
    ApiResponse deleteSchedule(Long id);
}
