
package com.bus_ticket.dto.City;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CityDto {
    
    private Long id;
    
    @NotBlank(message = "City name is required")
    private String cityName;
}
