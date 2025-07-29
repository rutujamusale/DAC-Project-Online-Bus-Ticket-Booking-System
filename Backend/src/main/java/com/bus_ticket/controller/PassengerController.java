package com.bus_ticket.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bus_ticket.entities.Passenger;
import com.bus_ticket.services.PassengerService;


@RestController
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.createPassenger(passenger));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
/////////////////////////////////////////////////////////////////////////////////