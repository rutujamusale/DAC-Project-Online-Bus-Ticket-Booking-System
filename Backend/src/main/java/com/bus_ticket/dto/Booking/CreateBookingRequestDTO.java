package com.bus_ticket.dto.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequestDTO {
    private Long userId;
    private Long scheduleId;
    private List<PassengerSeatDTO> passengers;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerSeatDTO {
        private Long seatId;
        private String passengerName;
        private Integer passengerAge;
        private String passengerGender;
        private String passengerPhone;
        private String passengerEmail;
    }
} 