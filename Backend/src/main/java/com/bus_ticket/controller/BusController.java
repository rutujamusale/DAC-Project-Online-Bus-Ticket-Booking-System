package com.bus_ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bus_ticket.dto.Bus.BusResponseDTO;
import com.bus_ticket.dto.Bus.NewBusDTO;
import com.bus_ticket.dto.Bus.UpdateBusDTO;
import com.bus_ticket.services.BusService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/buses")
@AllArgsConstructor 
public class BusController {

    @Autowired
    private BusService busService;


    @GetMapping
    public ResponseEntity<?> getAllBuses() {
        List<BusResponseDTO> buses = busService.getAllBuses();
        if(buses.isEmpty())
			 return ResponseEntity
					 .status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(buses);
    }
    
     @GetMapping("/{id}")
    public ResponseEntity<?> getBusById(@PathVariable Long id) {
        BusResponseDTO bus = busService.getBusById(id);
        return ResponseEntity.ok(bus);
    }

    @PostMapping
    public ResponseEntity<?> addBus(@RequestBody NewBusDTO newBus) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(busService
                .createBus(newBus));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBus(@PathVariable Long id, @RequestBody UpdateBusDTO updateBus) {
        
        return ResponseEntity.ok(busService
                .updateBus(id, updateBus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBus(@PathVariable Long id){
        return ResponseEntity.ok(busService
        .deleteBus(id));
    }
}
