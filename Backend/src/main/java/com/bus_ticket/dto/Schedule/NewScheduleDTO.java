package com.bus_ticket.dto.Schedule;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewScheduleDTO {
    private Date departureTime;
    private Date arrivalTime;
    private int availableSeats;
    private Long routeId;
    private Long busId;
    private Long vendorId;
}
