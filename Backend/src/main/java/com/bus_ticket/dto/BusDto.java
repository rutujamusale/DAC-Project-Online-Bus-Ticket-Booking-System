package com.bus_ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Bus data transfer object")
public class BusDto {
    
    @Schema(description = "Bus ID", example = "1")
    private Long id;
    
    @NotBlank(message = "Bus number is required")
    @Schema(description = "Bus number", example = "MH12AB1234")
    private String busNumber;
    
    @NotBlank(message = "Bus name is required")
    @Schema(description = "Bus name", example = "Express Bus")
    private String busName;
    
    @NotBlank(message = "Source is required")
    @Schema(description = "Source city", example = "Mumbai")
    private String source;
    
    @NotBlank(message = "Destination is required")
    @Schema(description = "Destination city", example = "Pune")
    private String destination;
    
    @NotNull(message = "Departure time is required")
    @Schema(description = "Departure time", example = "08:00")
    private LocalTime departureTime;
    
    @NotNull(message = "Arrival time is required")
    @Schema(description = "Arrival time", example = "11:30")
    private LocalTime arrivalTime;
    
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    @Schema(description = "Total seats", example = "40")
    private Integer totalSeats;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Schema(description = "Price per seat", example = "450.00")
    private BigDecimal price;
    
    @Schema(description = "Bus type", example = "AC")
    private String busType;
    
    @Schema(description = "Vendor ID", example = "1")
    private Long vendorId;
}
