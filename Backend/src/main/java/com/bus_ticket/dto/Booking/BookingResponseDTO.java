package com.bus_ticket.dto.Booking;

import com.bus_ticket.entities.Transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;

    private Transaction transaction;
    
    private Double price;
    
    private Integer seat_col;
    
    private Integer seat_row;
}
