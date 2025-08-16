
package com.bus_ticket.services;

import com.bus_ticket.dao.SeatDao;
import com.bus_ticket.dao.ScheduleDao;
import com.bus_ticket.dto.ApiResponse;
import com.bus_ticket.dto.Seat.SeatResponseDTO;
import com.bus_ticket.dto.Seat.SeatSelectionRequestDTO;
import com.bus_ticket.entities.Seat;
import com.bus_ticket.entities.Schedule;
import com.bus_ticket.entities.Booking;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {
    
    @Autowired
    private SeatDao seatDao;
    
    @Autowired
    private ScheduleDao scheduleDao;
    
    @Override
    public List<SeatResponseDTO> getSeatsByScheduleId(Long scheduleId) {
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        
        List<Seat> seats = seatDao.findByScheduleId(scheduleId);
        
        return seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ApiResponse selectSeats(SeatSelectionRequestDTO request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + request.getScheduleId()));
        
        List<Seat> seats = seatDao.findAllById(request.getSelectedSeatIds());
        
        // Check if all seats are available
        for (Seat seat : seats) {
            if (!seat.getStatus().equals(Seat.SeatStatus.AVAILABLE)) {
                return new ApiResponse(false, "Seat " + seat.getSeatNumber() + " is not available");
            }
            if (!seat.getSchedule().getId().equals(request.getScheduleId())) {
                return new ApiResponse(false, "Invalid seat selection");
            }
        }
        
        // Don't lock seats immediately - just return success
        // Seats will be locked only after user authentication and confirmation
        return new ApiResponse(true, "Seats selected successfully. Please login to confirm booking.");
    }
    
    @Override
    @Transactional
    public ApiResponse lockSeats(SeatSelectionRequestDTO request) {
        try {
            Schedule schedule = scheduleDao.findById(request.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + request.getScheduleId()));
            
            List<Seat> seats = seatDao.findAllById(request.getSelectedSeatIds());
            
            // Check if all seats are still available
            for (Seat seat : seats) {
                if (!seat.getStatus().equals(Seat.SeatStatus.AVAILABLE)) {
                    return new ApiResponse(false, "Seat " + seat.getSeatNumber() + " is no longer available");
                }
                if (!seat.getSchedule().getId().equals(request.getScheduleId())) {
                    return new ApiResponse(false, "Invalid seat selection");
                }
            }
            
            // Now lock the seats by setting status to RESERVED and setting lockedAt timestamp
            LocalDateTime now = LocalDateTime.now();
            for (Seat seat : seats) {
                seat.setStatus(Seat.SeatStatus.RESERVED);
                seat.setLockedAt(now);
                seatDao.save(seat);
            }
            
            System.out.println("Successfully locked " + seats.size() + " seats for schedule " + request.getScheduleId());
            return new ApiResponse(true, "Seats locked successfully");
        } catch (Exception e) {
            System.err.println("Error locking seats: " + e.getMessage());
            return new ApiResponse(false, "Error locking seats: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ApiResponse releaseSeats(Long scheduleId, List<Long> seatIds) {
        List<Seat> seats = seatDao.findAllById(seatIds);
        
        for (Seat seat : seats) {
            if (seat.getSchedule().getId().equals(scheduleId) && 
                seat.getStatus().equals(Seat.SeatStatus.RESERVED)) {
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                seat.setLockedAt(null);
                seatDao.save(seat);
            }
        }
        
        return new ApiResponse(true, "Seats released successfully");
    }
    
    @Override
    public Long getAvailableSeatCount(Long scheduleId) {
        return seatDao.countAvailableSeatsByScheduleId(scheduleId);
    }
    
    @Override
    @Transactional
    public void unlockExpiredSeats() {
        try {
            // Unlock seats that have been RESERVED (not BOOKED) for more than 10 minutes
            // BOOKED seats should never be unlocked as they represent completed bookings
            LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(10);
            System.out.println("Checking for expired seats before: " + expiryTime);
            
            List<Seat> expiredSeats = seatDao.findExpiredReservedSeats(expiryTime);
            System.out.println("Found " + expiredSeats.size() + " expired reserved seats");
            
            int unlockedCount = 0;
            for (Seat seat : expiredSeats) {
                System.out.println("Processing seat: " + seat.getSeatNumber() + " (Status: " + seat.getStatus() + ", LockedAt: " + seat.getLockedAt() + ")");
                
                // Double-check that the seat is still RESERVED before unlocking
                // This prevents unlocking seats that might have been booked between query and processing
                if (seat.getStatus().equals(Seat.SeatStatus.RESERVED)) {
                    // Check if this seat is associated with a pending booking
                    // If yes, we should not unlock it immediately
                    boolean hasPendingBooking = false;
                    if (seat.getBookingSeats() != null && !seat.getBookingSeats().isEmpty()) {
                        hasPendingBooking = seat.getBookingSeats().stream()
                            .anyMatch(bs -> bs.getBooking() != null && 
                                (bs.getBooking().getStatus() == Booking.BookingStatus.PENDING || 
                                 bs.getBooking().getStatus() == Booking.BookingStatus.PAYMENT_FAILED));
                    }
                    
                    // Also check if the seat has been locked for more than 10 minutes
                    boolean isExpired = seat.getLockedAt() != null && 
                        seat.getLockedAt().isBefore(LocalDateTime.now().minusMinutes(10));
                    
                    if (!hasPendingBooking && isExpired) {
                        seat.setStatus(Seat.SeatStatus.AVAILABLE);
                        seat.setLockedAt(null);
                        seatDao.save(seat);
                        unlockedCount++;
                        System.out.println("✅ Unlocked expired seat: " + seat.getSeatNumber());
                    } else {
                        System.out.println("⚠️ Seat " + seat.getSeatNumber() + " has pending booking, not unlocking");
                    }
                } else {
                    System.out.println("⚠️ Seat " + seat.getSeatNumber() + " is not RESERVED (Status: " + seat.getStatus() + "), skipping");
                }
            }
            
            System.out.println("Auto-unlock completed: " + unlockedCount + " seats unlocked out of " + expiredSeats.size() + " expired seats");
        } catch (Exception e) {
            System.err.println("Error in unlockExpiredSeats: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    private SeatResponseDTO convertToDTO(Seat seat) {
        return new SeatResponseDTO(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getRowNumber(),
                seat.getColumnNumber(),
                seat.getSeatType().name(),
                seat.getStatus().name(),
                seat.getPrice(),
                false // isSelected will be set by frontend
        );
    }
} 



