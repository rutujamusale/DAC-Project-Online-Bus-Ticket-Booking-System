package com.bus_ticket.dto.Bus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Bus search request")
public class BusSearchRequest {
    
    @NotBlank(message = "Source is required")
    @Schema(description = "Source city", example = "Mumbai")
    private String source;
    
    @NotBlank(message = "Destination is required")
    @Schema(description = "Destination city", example = "Pune")
    private String destination;
    
    @NotNull(message = "Travel date is required")
    @Schema(description = "Travel date", example = "2024-01-15")
    private LocalDate travelDate;
}
