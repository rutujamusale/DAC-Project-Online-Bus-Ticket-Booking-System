package com.bus_ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.NewBookingDTO;
import com.bus_ticket.services.BookingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor 
public class BookingController {

    @Autowired
    private BookingService BookingService;


    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<BookingResponseDTO> Bookinges = BookingService.getAllBookings();
        if(Bookinges.isEmpty())
			 return ResponseEntity
					 .status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(Bookinges);
    }
    
     @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        BookingResponseDTO Booking = BookingService.getBookingById(id);
        return ResponseEntity.ok(Booking);
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody NewBookingDTO newBooking) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(BookingService
                .createBooking(newBooking));
    }
    
    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody UpdateBookingDTO updateBooking) {
        
    //     return ResponseEntity.ok(BookingService
    //             .updateBooking(id, updateBooking));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id){
        return ResponseEntity.ok(BookingService
        .deleteBooking(id));
    }
}
////////////////////////////////////////////////////////////////////////////