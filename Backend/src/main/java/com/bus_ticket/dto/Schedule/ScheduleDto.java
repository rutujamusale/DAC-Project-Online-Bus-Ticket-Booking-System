package com.bus_ticket.dto.Schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto {
    
    private Long id;
    
    @NotNull(message = "Bus ID is required")
    private Long busId;
    
    @NotBlank(message = "Source is required")
    private String source;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Schedule date is required")
    private LocalDate scheduleDate;
    
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;
    
    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;
    
    private Integer availableSeats;
    
    private boolean active = true;
    
    // Bus details for display
    private String busName;
    private String busNumber;
    private String price;
    private String busType;
}
