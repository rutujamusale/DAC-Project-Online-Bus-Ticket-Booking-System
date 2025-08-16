
package com.bus_ticket.controller;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Seat.SeatResponseDTO;
import com.bus_ticket.dto.Seat.SeatSelectionRequestDTO;
import com.bus_ticket.services.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Seat Management", description = "APIs for seat operations")
public class SeatController {
    
    @Autowired
    private SeatService seatService;
    
    @GetMapping("/schedule/{scheduleId}")
    @Operation(summary = "Get seats by schedule ID", description = "Get all seats for a specific schedule")
    public ResponseEntity<List<SeatResponseDTO>> getSeatsByScheduleId(@PathVariable Long scheduleId) {
        List<SeatResponseDTO> seats = seatService.getSeatsByScheduleId(scheduleId);
        return ResponseEntity.ok(seats);
    }
    
    @PostMapping("/select")
    @Operation(summary = "Select seats", description = "Select seats for a schedule (does not lock seats)")
    public ResponseEntity<ApiResponse> selectSeats(@RequestBody SeatSelectionRequestDTO request) {
        ApiResponse response = seatService.selectSeats(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/lock")
    @Operation(summary = "Lock seats", description = "Lock selected seats after user authentication")
    public ResponseEntity<ApiResponse> lockSeats(@RequestBody SeatSelectionRequestDTO request) {
        ApiResponse response = seatService.lockSeats(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/release")
    @Operation(summary = "Release seats", description = "Release selected seats")
    public ResponseEntity<ApiResponse> releaseSeats(
            @RequestParam Long scheduleId,
            @RequestBody List<Long> seatIds) {
        ApiResponse response = seatService.releaseSeats(scheduleId, seatIds);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/unlock-expired")
    @Operation(summary = "Unlock expired seats", description = "Unlock seats that have been reserved for more than 10 minutes")
    public ResponseEntity<ApiResponse> unlockExpiredSeats() {
        seatService.unlockExpiredSeats();
        return ResponseEntity.ok(new ApiResponse(true, "Expired seats unlocked successfully"));
    }
    
    @PostMapping("/test-unlock")
    @Operation(summary = "Test unlock expired seats", description = "Manually trigger unlock for testing purposes")
    public ResponseEntity<ApiResponse> testUnlockExpiredSeats() {
        System.out.println("🧪 Manual test unlock triggered at: " + java.time.LocalDateTime.now());
        seatService.unlockExpiredSeats();
        return ResponseEntity.ok(new ApiResponse(true, "Test unlock completed successfully"));
    }
    

    
    @GetMapping("/available-count/{scheduleId}")
    @Operation(summary = "Get available seat count", description = "Get count of available seats for a schedule")
    public ResponseEntity<Long> getAvailableSeatCount(@PathVariable Long scheduleId) {
        Long count = seatService.getAvailableSeatCount(scheduleId);
        return ResponseEntity.ok(count);
    }
} 