package com.bus_ticket.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SeatAutoUnlockService {
    
    @Autowired
    private SeatService seatService;
    
    // Run every 5 minutes to check for expired seats
    @Scheduled(fixedRate = 300000) // 300000 milliseconds = 5 minutes
    public void unlockExpiredSeats() {
        try {
            System.out.println("🕐 Auto-unlock service running at: " + java.time.LocalDateTime.now());
            seatService.unlockExpiredSeats();
            System.out.println("✅ Auto-unlock service completed successfully");
        } catch (Exception e) {
            System.err.println("❌ Error unlocking expired seats: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 