package com.bus_ticket.dto.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponseDTO {
    private Long id;
    private String seatNumber;
    private Integer rowNumber;
    private Integer columnNumber;
    private String seatType;
    private String status;
    private Double price;
    private Boolean isSelected;
} 