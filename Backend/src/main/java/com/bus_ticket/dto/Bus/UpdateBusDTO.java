package com.bus_ticket.dto.Bus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Updated Bus data transfer object")
public class UpdateBusDTO {
    
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
}
