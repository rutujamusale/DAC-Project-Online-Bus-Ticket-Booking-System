package com.bus_ticket.services;

import com.bus_ticket.dto.Feedback.FeedbackDTO;
import com.bus_ticket.dto.Feedback.FeedbackRequestDTO;
import com.bus_ticket.dto.Feedback.FeedbackResponseDTO;
import java.util.List;
import java.util.Map;

public interface FeedbackService {
    
    // Enhanced feedback methods
    FeedbackResponseDTO createEnhancedFeedback(FeedbackRequestDTO feedbackRequestDTO);
    FeedbackResponseDTO getEnhancedFeedbackById(Long id);
    List<FeedbackResponseDTO> getAllEnhancedFeedback();
    List<FeedbackResponseDTO> getEnhancedFeedbackByUserId(Long userId);
    List<FeedbackResponseDTO> getEnhancedFeedbackByBookingId(Long bookingId);
    List<FeedbackResponseDTO> getEnhancedFeedbackByBusName(String busName);
    List<FeedbackResponseDTO> getEnhancedFeedbackByCategory(String category);
    FeedbackResponseDTO updateEnhancedFeedback(Long id, FeedbackRequestDTO feedbackRequestDTO);
    void deleteEnhancedFeedback(Long id);
    
    // Legacy methods for backward compatibility
    FeedbackDTO createFeedback(FeedbackDTO feedbackDTO);
    FeedbackDTO getFeedbackById(Long id);
    List<FeedbackDTO> getAllFeedback();
    List<FeedbackDTO> getFeedbackByUserId(Long userId);
    List<FeedbackDTO> getFeedbackByBookingId(Long bookingId);
    List<FeedbackDTO> getFeedbackByBusName(String busName);
    List<FeedbackDTO> getFeedbackByCategory(String category);
    FeedbackDTO updateFeedback(Long id, FeedbackDTO feedbackDTO);
    void deleteFeedback(Long id);
    
    // Statistics methods
    Map<String, Object> getFeedbackStatistics();
    Map<String, Object> getFeedbackStatisticsByBus(String busName);
} 