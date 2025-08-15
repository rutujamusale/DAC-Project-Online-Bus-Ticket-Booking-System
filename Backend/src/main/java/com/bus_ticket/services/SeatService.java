
package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Seat.SeatResponseDTO;
import com.bus_ticket.dto.Seat.SeatSelectionRequestDTO;

import java.util.List;

public interface SeatService {
    
    List<SeatResponseDTO> getSeatsByScheduleId(Long scheduleId);
    
    ApiResponse selectSeats(SeatSelectionRequestDTO request);
    
    ApiResponse lockSeats(SeatSelectionRequestDTO request);
    
    ApiResponse releaseSeats(Long scheduleId, List<Long> seatIds);
    
    Long getAvailableSeatCount(Long scheduleId);
    
    void unlockExpiredSeats();
    

} 