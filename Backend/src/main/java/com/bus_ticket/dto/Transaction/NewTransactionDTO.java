package com.bus_ticket.dto.Transaction;

import com.bus_ticket.entities.Schedule;
import com.bus_ticket.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class NewTransactionDTO {

    private Schedule schedule;
    
    private User user;
}
