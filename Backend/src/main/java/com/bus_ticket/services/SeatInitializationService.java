package com.bus_ticket.services;

import com.bus_ticket.dao.SeatDao;
import com.bus_ticket.entities.Seat;
import com.bus_ticket.entities.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatInitializationService {
    
    @Autowired
    private SeatDao seatDao;
    
    @Transactional
    public void initializeSeatsForSchedule(Schedule schedule) {
        int totalSeats = schedule.getBus().getTotalSeats();
        List<Seat> seats = new ArrayList<>();
        
        // Calculate rows and columns (assuming 4 seats per row)
        int seatsPerRow = 4;
        int totalRows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        
        int seatCounter = 1;
        double basePrice = schedule.getBus().getPrice().doubleValue();
        
        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= seatsPerRow && seatCounter <= totalSeats; col++) {
                String seatNumber = String.format("%02d", seatCounter);
                
                // Determine seat type based on position
                Seat.SeatType seatType;
                if (col == 1 || col == 4) {
                    seatType = Seat.SeatType.WINDOW;
                } else if (col == 2 || col == 3) {
                    seatType = Seat.SeatType.AISLE;
                } else {
                    seatType = Seat.SeatType.MIDDLE;
                }
                
                // Calculate price based on seat type and position
                double seatPrice = calculateSeatPrice(basePrice, seatType, row, col);
                
                Seat seat = new Seat(schedule, seatNumber, row, col, seatType, seatPrice);
                seats.add(seat);
                seatCounter++;
            }
        }
        
        seatDao.saveAll(seats);
    }
    
    private double calculateSeatPrice(double basePrice, Seat.SeatType seatType, int row, int col) {
        double price = basePrice;
        
        // Premium for window seats
        if (seatType == Seat.SeatType.WINDOW) {
            price += basePrice * 0.1; // 10% premium
        }
        
        // Premium for front rows (better view)
        if (row <= 3) {
            price += basePrice * 0.05; // 5% premium
        }
        
        // Premium for aisle seats (easier access)
        if (seatType == Seat.SeatType.AISLE) {
            price += basePrice * 0.05; // 5% premium
        }
        
        return Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
    }
} 