package com.bus_ticket.dto.Transaction;

import java.util.List;

import com.bus_ticket.entities.Passenger;
import com.bus_ticket.entities.Payment;
import com.bus_ticket.entities.Schedule;
import com.bus_ticket.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {
    private Long id;

    private Schedule schedule;
    
    private User user;

    private List<Passenger> passengers;

    private Payment payment;
}
