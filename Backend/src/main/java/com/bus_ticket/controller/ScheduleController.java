
package com.bus_ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Schedule.ScheduleDto;
import com.bus_ticket.services.ScheduleService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Schedule Management", description = "APIs for schedule operations")
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @PostMapping
    @Operation(summary = "Add new schedule", description = "Add a new schedule for a bus")
    public ResponseEntity<?> addSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        ApiResponse response = scheduleService.addSchedule(scheduleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/bus/{busId}")
    @Operation(summary = "Get schedules by bus ID", description = "Get all schedules for a specific bus")
    public ResponseEntity<?> getSchedulesByBusId(@PathVariable Long busId) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByBusId(busId);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get schedules by vendor ID", description = "Get all schedules for a specific vendor")
    public ResponseEntity<?> getSchedulesByVendorId(@PathVariable Long vendorId) {
        List<ScheduleDto> schedules = scheduleService.getSchedulesByVendorId(vendorId);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search scheduled buses", description = "Search for scheduled buses by route and date")
    public ResponseEntity<?> searchScheduledBuses(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ScheduleDto> schedules = scheduleService.searchScheduledBuses(source, destination, date);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Get schedule details by ID")
    public ResponseEntity<?> getScheduleById(@PathVariable Long id) {
        ScheduleDto schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update schedule", description = "Update schedule information")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleDto scheduleDto) {
        ApiResponse response = scheduleService.updateSchedule(id, scheduleDto);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule", description = "Delete a schedule")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        ApiResponse response = scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(response);
    }
}
