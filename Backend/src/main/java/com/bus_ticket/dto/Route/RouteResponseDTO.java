package com.bus_ticket.dto.Route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RouteResponseDTO {

    private Long id;
    private Long vendorId;
    private String name;
    private Long sourceCityId;
    private Long destinationCityId;     
}
