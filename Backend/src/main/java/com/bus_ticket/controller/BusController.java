package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Bus.BusDto;
import com.bus_ticket.services.BusService;
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
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bus", description = "Permanently delete a bus and all its schedules")
    public ResponseEntity<?> deleteBus(@PathVariable Long id) {
        System.out.println("=== DELETE BUS REQUEST ===");
        System.out.println("Received delete request for bus ID: " + id);
        System.out.println("Request timestamp: " + new java.util.Date());
        
        try {
            ApiResponse response = busService.deleteBus(id);
            System.out.println("Delete response: " + response.getMessage());
            System.out.println("Success: " + response.isSuccess());
            System.out.println("=== DELETE BUS COMPLETED ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error in delete endpoint: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse(false, "Internal server error: " + e.getMessage()));
        }
    }
    
    // Test endpoint to verify backend is working
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(new ApiResponse(true, "Backend is working!"));
    }
    
    // Test endpoint to check database connection
    @GetMapping("/test-db")
    public ResponseEntity<?> testDatabase() {
        try {
            // Try to get a count of buses to test database connection
            long busCount = busService.getBusesByVendor(1L).size(); // This will throw an exception if DB is not accessible
            return ResponseEntity.ok(new ApiResponse(true, "Database connection successful. Bus count for vendor 1: " + busCount));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, "Database connection failed: " + e.getMessage()));
        }
    }
}
