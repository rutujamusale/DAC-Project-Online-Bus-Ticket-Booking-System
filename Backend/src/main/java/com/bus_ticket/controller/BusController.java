package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.dto.Bus.BusSearchRequest;
import com.bus_ticket.services.BusService;
import com.bus_ticket.services.ScheduleService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Bus Management", description = "APIs for bus operations")
public class BusController {
    
    @Autowired
    private BusService busService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @PostMapping
    @Operation(summary = "Add new bus", description = "Add a new bus to the system")
    public ResponseEntity<?> addBus(@Valid @RequestBody BusDto busDto) {
        ApiResponse response = busService.addBus(busDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get buses by vendor", description = "Get all buses for a specific vendor")
    public ResponseEntity<?> getBusesByVendor(@PathVariable Long vendorId) {
        List<BusDto> buses = busService.getBusesByVendor(vendorId);
        return ResponseEntity.ok(buses);
    }
    
    @GetMapping
    @Operation(summary = "Get all buses", description = "Get all active buses")
    public ResponseEntity<?> getAllBuses() {
        List<BusDto> buses = busService.getAllActiveBuses();
        return ResponseEntity.ok(buses);
    }
    
    @PostMapping("/search")
    @Operation(summary = "Search buses", description = "Search scheduled buses by source, destination and date")
    public ResponseEntity<?> searchBuses(@Valid @RequestBody BusSearchRequest searchRequest) {
        // Use schedule service to get only scheduled buses
        var schedules = scheduleService.searchScheduledBuses(
            searchRequest.getSource(), 
            searchRequest.getDestination(), 
            searchRequest.getTravelDate()
        );
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get bus by ID", description = "Get bus details by ID")
    public ResponseEntity<?> getBusById(@PathVariable Long id) {
        BusDto bus = busService.getBusById(id);
        return ResponseEntity.ok(bus);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update bus", description = "Update bus information")
    public ResponseEntity<?> updateBus(@PathVariable Long id, @Valid @RequestBody BusDto busDto) {
        ApiResponse response = busService.updateBus(id, busDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bus", description = "Delete a bus and all its schedules")
    public ResponseEntity<?> deleteBus(@PathVariable Long id) {
        ApiResponse response = busService.softDeleteBus(id);
        return ResponseEntity.ok(response);
    }
}
