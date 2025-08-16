package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bus_ticket.entities.FeedBack;
import java.util.List;

@Repository
public interface FeedbackDao extends JpaRepository<FeedBack, Long> {
    List<FeedBack> findByUserId(Long userId);
    List<FeedBack> findByBookingId(Long bookingId);
    List<FeedBack> findByBusName(String busName);
    List<FeedBack> findByCategory(String category);
    List<FeedBack> findByIsActiveTrue();
} 