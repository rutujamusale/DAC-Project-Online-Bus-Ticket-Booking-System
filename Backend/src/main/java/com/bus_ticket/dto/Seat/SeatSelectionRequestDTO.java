package com.bus_ticket.dto.Seat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectionRequestDTO {
    private Long scheduleId;
    private List<Long> selectedSeatIds;
} 