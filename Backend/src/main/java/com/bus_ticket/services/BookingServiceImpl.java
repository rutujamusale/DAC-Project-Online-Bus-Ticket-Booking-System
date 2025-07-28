package com.bus_ticket.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import com.bus_ticket.dao.BookingDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.NewBookingDTO;
import com.bus_ticket.entities.Booking;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingDao BookingDao;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Booking createBooking(NewBookingDTO newBooking) {
        Booking entity = modelMapper.map(newBooking,Booking.class);
        return BookingDao.save(entity);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return BookingDao.findByActiveBookingTrue() //List<entity>
				.stream() //Stream<entity>
				.map(Booking -> 
				modelMapper.map(Booking, BookingResponseDTO.class)) //Stream<dto>
				.toList();
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking entity = BookingDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid Booking ID !!!!!"));
        return modelMapper.map(entity, BookingResponseDTO.class);
    }


    @Override
    public ApiResponse deleteBooking(Long id) {
        Booking entity = BookingDao.findById(id)
            .orElseThrow(() ->
            new ResourceNotFoundException("Invalid Booking ID"));

        entity.setActiveBooking(false);
        return new ApiResponse("Booking deleted!!");
    }
    
}
