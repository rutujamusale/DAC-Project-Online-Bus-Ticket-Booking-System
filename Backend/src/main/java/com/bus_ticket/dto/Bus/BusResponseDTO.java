package com.bus_ticket.dto.Bus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusResponseDTO {
    
    @Schema(description = "Bus id")
    private Long id;
     
    @NotBlank(message = "Bus number is required")
    @Schema(description = "Bus number", example = "MH12AB1234")
    private String busNumber;
    
    @NotBlank(message = "Bus name is required")
    @Schema(description = "Bus name", example = "Express Bus")
    private String busName;
        
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    @Schema(description = "Total seats", example = "40")
    private Integer seatRows;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    @Schema(description = "Total seats", example = "40")
    private Integer seatCols;
    
    @Schema(description = "Bus type", example = "AC")
    private Long busTypeId;
    
    @Schema(description = "Vendor ID", example = "1")
    private Long vendorId;
}
