package com.bus_ticket.services;

import com.bus_ticket.dao.BookingDao;
import com.bus_ticket.dao.BookingSeatDao;
import com.bus_ticket.dao.SeatDao;
import com.bus_ticket.entities.Booking;
import com.bus_ticket.entities.BookingSeat;
import com.bus_ticket.entities.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingCleanupService {
    
    @Autowired
    private BookingDao bookingDao;
    
    @Autowired
    private BookingSeatDao bookingSeatDao;
    
    @Autowired
    private SeatDao seatDao;
    
    @Scheduled(fixedRate = 300000) // 5 minutes
    @Transactional
    public void cleanupExpiredPendingBookings() {
        try {
            LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(15);
            List<Booking> pendingBookings = bookingDao.findAllPendingBookings();
            
            int cleanedCount = 0;
            
            for (Booking booking : pendingBookings) {
                if (booking.getBookingDate().isBefore(expiryTime)) {
                    List<BookingSeat> bookingSeats = bookingSeatDao.findByBookingId(booking.getId());
                    
                    for (BookingSeat bookingSeat : bookingSeats) {
                        Seat seat = bookingSeat.getSeat();
                        if (seat.getStatus() == Seat.SeatStatus.RESERVED) {
                            seat.setStatus(Seat.SeatStatus.AVAILABLE);
                            seat.setLockedAt(null);
                            seatDao.save(seat);
                        }
                    }
                    
                    booking.setStatus(Booking.BookingStatus.CANCELLED);
                    bookingDao.save(booking);
                    cleanedCount++;
                }
            }
            
        } catch (Exception e) {
            // Log error silently to avoid console spam
        }
    }
} 