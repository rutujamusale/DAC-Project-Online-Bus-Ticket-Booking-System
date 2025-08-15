package com.bus_ticket.dto.Booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeatResponseDTO {
    private Long id;
    private String seatNumber;
    private String passengerName;
    private Integer passengerAge;
    private String passengerGender;
    private String passengerPhone;
    private String passengerEmail;
    private Double seatPrice;
} 