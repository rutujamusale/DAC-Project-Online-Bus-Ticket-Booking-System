package com.bus_ticket.dto.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewBookingDTO {
    
    private Double price;
    
    private Integer seat_col;
    
    private Integer seat_row;
}
