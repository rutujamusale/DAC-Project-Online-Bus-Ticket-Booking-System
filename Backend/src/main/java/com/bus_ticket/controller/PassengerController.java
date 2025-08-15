package com.bus_ticket.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bus_ticket.entities.Passenger;
import com.bus_ticket.services.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@Tag(name = "Passenger Management", description = "APIs for passenger operations")
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    @Operation(summary = "Create passenger", description = "Create a new passenger")
    public ResponseEntity<?> create(@RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.createPassenger(passenger));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get passenger by ID", description = "Get passenger details by ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @GetMapping
    @Operation(summary = "Get all passengers", description = "Get all passengers")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get passengers by booking ID", description = "Get all passengers for a specific booking")
    public ResponseEntity<?> getByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(passengerService.getPassengersByBookingId(bookingId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update passenger", description = "Update passenger details")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete passenger", description = "Delete a passenger")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
/////////////////////////////////////////////////////////////////////////////////