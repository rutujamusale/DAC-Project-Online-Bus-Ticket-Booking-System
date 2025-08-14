package com.bus_ticket.services;

import com.bus_ticket.dao.*;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Booking.BookingResponseDTO;
import com.bus_ticket.dto.Booking.BookingSeatResponseDTO;
import com.bus_ticket.dto.Booking.CreateBookingRequestDTO;
import com.bus_ticket.entities.*;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private BookingSeatDao bookingSeatDao;

    @Autowired
    private SeatDao seatDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PassengerDao passengerDao;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(CreateBookingRequestDTO request) {
        try {
            if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
                throw new IllegalArgumentException("At least one passenger is required");
            }

            if (request.getUserId() == null) {
                throw new IllegalArgumentException("User ID is required");
            }

            if (request.getScheduleId() == null) {
                throw new IllegalArgumentException("Schedule ID is required");
            }

            User user = userDao.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

            Seat firstSeat = seatDao.findById(request.getPassengers().get(0).getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

            // Validate all seats are still available/reserved
            for (CreateBookingRequestDTO.PassengerSeatDTO passenger : request.getPassengers()) {
                Seat seat = seatDao.findById(passenger.getSeatId())
                        .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + passenger.getSeatId()));
                
                if (!seat.getStatus().equals(Seat.SeatStatus.RESERVED) && !seat.getStatus().equals(Seat.SeatStatus.AVAILABLE)) {
                    throw new IllegalArgumentException("Seat " + seat.getSeatNumber() + " is no longer available for booking");
                }
            }

            double totalAmount = request.getPassengers().stream()
                    .mapToDouble(passenger -> {
                        Seat seat = seatDao.findById(passenger.getSeatId())
                                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
                        return seat.getPrice();
                    }).sum();

            Booking booking = new Booking(user, firstSeat.getSchedule(), totalAmount);
            Booking savedBooking = bookingDao.save(booking);

            List<BookingSeat> bookingSeats = request.getPassengers().stream()
                    .map(passenger -> {
                        Seat seat = seatDao.findById(passenger.getSeatId())
                                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + passenger.getSeatId()));

                        // Validate passenger data before creating BookingSeat
                        if (passenger.getPassengerName() == null || passenger.getPassengerName().trim().isEmpty()) {
                            throw new IllegalArgumentException("Passenger name cannot be empty");
                        }
                        if (passenger.getPassengerAge() == null || passenger.getPassengerAge() < 1 || passenger.getPassengerAge() > 120) {
                            throw new IllegalArgumentException("Invalid passenger age: " + passenger.getPassengerAge());
                        }
                        if (passenger.getPassengerGender() == null || passenger.getPassengerGender().trim().isEmpty()) {
                            throw new IllegalArgumentException("Passenger gender cannot be empty");
                        }
                        if (passenger.getPassengerPhone() == null || passenger.getPassengerPhone().trim().isEmpty()) {
                            throw new IllegalArgumentException("Passenger phone cannot be empty");
                        }
                        if (passenger.getPassengerEmail() == null || passenger.getPassengerEmail().trim().isEmpty()) {
                            throw new IllegalArgumentException("Passenger email cannot be empty");
                        }

                        BookingSeat bookingSeat = new BookingSeat(
                                savedBooking,
                                seat,
                                passenger.getPassengerName().trim(),
                                passenger.getPassengerAge(),
                                passenger.getPassengerGender().trim(),
                                passenger.getPassengerPhone().trim(),
                                passenger.getPassengerEmail().trim()
                        );
                        return bookingSeat;
                    }).collect(Collectors.toList());

            List<BookingSeat> savedBookingSeats = bookingSeatDao.saveAll(bookingSeats);

            // Keep seats as RESERVED until payment is completed
            for (BookingSeat bookingSeat : savedBookingSeats) {
                Seat seat = bookingSeat.getSeat();
                seat.setStatus(Seat.SeatStatus.RESERVED);
                seat.setLockedAt(null);
                seatDao.save(seat);
            }

            List<Passenger> passengers = request.getPassengers().stream()
                    .map(passenger -> {
                        Passenger passengerEntity = new Passenger();
                        passengerEntity.setBooking(savedBooking);
                        passengerEntity.setName(passenger.getPassengerName().trim());
                        passengerEntity.setAge(passenger.getPassengerAge());
                        passengerEntity.setGender(passenger.getPassengerGender().trim());

                        // Format phone number
                        String phoneNumber = passenger.getPassengerPhone().trim();
                        if (phoneNumber != null) {
                            // Remove all non-numeric characters
                            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                            // Ensure it's 10 digits
                            if (phoneNumber.length() == 10) {
                                phoneNumber = phoneNumber;
                            } else if (phoneNumber.length() > 10) {
                                phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
                            } else {
                                throw new IllegalArgumentException("Invalid phone number: " + passenger.getPassengerPhone());
                            }
                        }
                        passengerEntity.setContact(phoneNumber);
                        passengerEntity.setEmail(passenger.getPassengerEmail().trim());
                        passengerEntity.setUid(UUID.randomUUID().toString());
                        return passengerEntity;
                    }).collect(Collectors.toList());

            List<Passenger> savedPassengers = passengerDao.saveAll(passengers);

            savedBooking.setBookingSeats(savedBookingSeats);
            savedBooking.setPassengers(savedPassengers);
            bookingDao.save(savedBooking);

            return convertToDTO(savedBooking);
        } catch (Exception e) {
            throw new RuntimeException("Error creating booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookingResponseDTO> getUserBookings(Long userId) {
        List<Booking> bookings = bookingDao.findByUserId(userId);
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApiResponse cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingDao.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            return new ApiResponse(false, "You can only cancel your own bookings");
        }

        // Allow cancellation of PENDING and PAYMENT_FAILED bookings
        if (booking.getStatus() != Booking.BookingStatus.PENDING && 
            booking.getStatus() != Booking.BookingStatus.PAYMENT_FAILED) {
            return new ApiResponse(false, "Only pending or payment failed bookings can be cancelled");
        }

        List<BookingSeat> bookingSeats = bookingSeatDao.findByBookingId(bookingId);
        for (BookingSeat bookingSeat : bookingSeats) {
            Seat seat = bookingSeat.getSeat();
            // Release seat back to available status
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seatDao.save(seat);
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingDao.save(booking);

        return new ApiResponse(true, "Booking cancelled successfully");
    }

    @Override
    @Transactional
    public ApiResponse unlockBookingSeats(Long bookingId, Long userId) {
        Booking booking = bookingDao.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            return new ApiResponse(false, "You can only unlock your own booking seats");
        }

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            return new ApiResponse(false, "Only pending bookings can be unlocked");
        }

        List<BookingSeat> bookingSeats = bookingSeatDao.findByBookingId(bookingId);
        for (BookingSeat bookingSeat : bookingSeats) {
            Seat seat = bookingSeat.getSeat();
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seatDao.save(seat);
        }

        return new ApiResponse(true, "Booking seats unlocked successfully");
    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        List<BookingSeatResponseDTO> bookingSeats = bookingSeatDao.findByBookingId(booking.getId())
                .stream()
                .map(this::convertBookingSeatToDTO)
                .collect(Collectors.toList());

        List<BookingResponseDTO.PassengerResponseDTO> passengers = passengerDao.findByBookingId(booking.getId())
                .stream()
                .map(this::convertPassengerToDTO)
                .collect(Collectors.toList());

        return new BookingResponseDTO(
                booking.getId(),
                booking.getSchedule().getId(),
                booking.getSchedule().getBus().getBusName(),
                booking.getSchedule().getSource(),
                booking.getSchedule().getDestination(),
                booking.getSchedule().getScheduleDate().toString(),
                booking.getSchedule().getDepartureTime().toString(),
                booking.getSchedule().getArrivalTime().toString(),
                booking.getTotalAmount(),
                booking.getStatus().name(),
                booking.getBookingDate(),
                booking.getPaymentStatus(),
                bookingSeats,
                passengers
        );
    }

    private BookingSeatResponseDTO convertBookingSeatToDTO(BookingSeat bookingSeat) {
        return new BookingSeatResponseDTO(
                bookingSeat.getId(),
                bookingSeat.getSeat().getSeatNumber(),
                bookingSeat.getPassengerName(),
                bookingSeat.getPassengerAge(),
                bookingSeat.getPassengerGender(),
                bookingSeat.getPassengerPhone(),
                bookingSeat.getPassengerEmail(),
                bookingSeat.getSeatPrice()
        );
    }

    private BookingResponseDTO.PassengerResponseDTO convertPassengerToDTO(Passenger passenger) {
        return new BookingResponseDTO.PassengerResponseDTO(
                passenger.getId(),
                passenger.getName(),
                passenger.getAge(),
                passenger.getGender(),
                passenger.getContact(),
                passenger.getEmail(),
                passenger.getUid()
        );
    }
}
