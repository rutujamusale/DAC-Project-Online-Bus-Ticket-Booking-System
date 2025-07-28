package com.bus_ticket.dto.City;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCityDTO {
    @NotBlank(message = "City name must not be blank")
    private String name;
}
