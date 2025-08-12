package com.bus_ticket.services;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.CreateBookingRequestDTO;

import java.util.List;

public interface BookingService {
    
    BookingResponseDTO createBooking(CreateBookingRequestDTO request);
    
    List<BookingResponseDTO> getUserBookings(Long userId);
    
    ApiResponse cancelBooking(Long bookingId, Long userId);
    
    ApiResponse unlockBookingSeats(Long bookingId, Long userId);
}