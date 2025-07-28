package com.bus_ticket.services;

import java.util.List;

import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.NewBookingDTO;
import com.bus_ticket.entities.Booking;

public interface BookingService {
    Booking createBooking(NewBookingDTO newBooking);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO getBookingById(Long id);
    ApiResponse deleteBooking(Long id);
}