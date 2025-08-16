package com.bus_ticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bus_ticket.entities.Feedback;
import java.util.List;

@Repository
public interface FeedbackDao extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUserId(Long userId);
    List<Feedback> findByBookingId(Long bookingId);
    List<Feedback> findByBusName(String busName);
    List<Feedback> findByCategory(String category);
    List<Feedback> findByIsActiveTrue();
} 