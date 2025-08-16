package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.City.CityDto;
import com.bus_ticket.services.CityService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "City Management", description = "APIs for city operations")
public class CityController {
    
    @Autowired
    private CityService cityService;
    
    @PostMapping
    @Operation(summary = "Add new city", description = "Add a new city to the system")
    public ResponseEntity<?> addCity(@Valid @RequestBody CityDto cityDto) {
        ApiResponse response = cityService.addCity(cityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all cities", description = "Get all cities")
    public ResponseEntity<?> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
    
    @GetMapping("/names")
    @Operation(summary = "Get all city names", description = "Get all city names for dropdown")
    public ResponseEntity<?> getAllCityNames() {
        List<String> cityNames = cityService.getAllCityNames();
        return ResponseEntity.ok(cityNames);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get city by ID", description = "Get city details by ID")
    public ResponseEntity<?> getCityById(@PathVariable Long id) {
        CityDto city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update city", description = "Update city information")
    public ResponseEntity<?> updateCity(@PathVariable Long id, @Valid @RequestBody CityDto cityDto) {
        ApiResponse response = cityService.updateCity(id, cityDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete city", description = "Delete a city")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        ApiResponse response = cityService.deleteCity(id);
        return ResponseEntity.ok(response);
    }
}
