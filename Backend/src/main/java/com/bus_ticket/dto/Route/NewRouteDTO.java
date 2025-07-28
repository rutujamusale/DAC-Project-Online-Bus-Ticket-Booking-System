package com.bus_ticket.dto.Route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class NewRouteDTO {
    private Long vendorId;
    private String name;
    private Long sourceCityId;
    private Long destinationCityId;
}
