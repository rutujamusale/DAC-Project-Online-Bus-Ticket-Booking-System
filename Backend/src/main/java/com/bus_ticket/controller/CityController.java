package com.bus_ticket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bus_ticket.dto.City.NewCityDTO;
import com.bus_ticket.services.CityService;

import lombok.AllArgsConstructor;
@RestController
@RequestMapping("/cities")
@AllArgsConstructor

public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody NewCityDTO cityDTO) {
        return ResponseEntity.ok(cityService.createCity(cityDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok("City deleted (soft delete applied).");
    }
}
