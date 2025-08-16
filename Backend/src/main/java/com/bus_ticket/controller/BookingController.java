package com.bus_ticket.controller;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.CreateBookingRequestDTO;
import com.bus_ticket.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Booking Management", description = "APIs for booking operations")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Create booking", description = "Create a new booking with passenger details")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody CreateBookingRequestDTO request) {
        BookingResponseDTO booking = bookingService.createBooking(request);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user bookings", description = "Get all bookings for a specific user")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancel a booking and unlock seats")
    public ResponseEntity<ApiResponse> cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId) {
        ApiResponse response = bookingService.cancelBooking(bookingId, userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{bookingId}/unlock")
    @Operation(summary = "Unlock booking seats", description = "Unlock seats for a booking without cancelling")
    public ResponseEntity<ApiResponse> unlockBookingSeats(
            @PathVariable Long bookingId,
            @RequestParam Long userId) {
        ApiResponse response = bookingService.unlockBookingSeats(bookingId, userId);
        return ResponseEntity.ok(response);
    }
    

}