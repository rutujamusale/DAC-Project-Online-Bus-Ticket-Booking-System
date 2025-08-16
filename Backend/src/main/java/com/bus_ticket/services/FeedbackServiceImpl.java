package com.bus_ticket.services;

import com.bus_ticket.dao.FeedbackDao;
import com.bus_ticket.dao.UserDao;
import com.bus_ticket.dao.BookingDao;
import com.bus_ticket.dto.Feedback.FeedbackDTO;
import com.bus_ticket.dto.Feedback.FeedbackRequestDTO;
import com.bus_ticket.dto.Feedback.FeedbackResponseDTO;
import com.bus_ticket.entities.FeedBack;
import com.bus_ticket.entities.User;
import com.bus_ticket.entities.Booking;
import com.bus_ticket.custom_exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    
    @Autowired
    private FeedbackDao feedbackDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BookingDao bookingDao;
    
    // Enhanced Feedback Methods
    
    @Override
    @Transactional
    public FeedbackResponseDTO createEnhancedFeedback(FeedbackRequestDTO feedbackRequestDTO) {
        // Validate input
        validateFeedbackRequest(feedbackRequestDTO);
        
        User user = userDao.findById(feedbackRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + feedbackRequestDTO.getUserId()));
        
        Booking booking = bookingDao.findById(feedbackRequestDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + feedbackRequestDTO.getBookingId()));
        
        // Check if feedback already exists for this booking
        List<FeedBack> existingFeedback = feedbackDao.findByBookingId(feedbackRequestDTO.getBookingId());
        if (!existingFeedback.isEmpty()) {
            throw new IllegalStateException("Feedback already exists for booking ID: " + feedbackRequestDTO.getBookingId());
        }
        
        FeedBack feedback = new FeedBack();
        feedback.setUser(user);
        feedback.setBooking(booking);
        feedback.setRating(feedbackRequestDTO.getOverallRating());
        feedback.setOverallExperience(feedbackRequestDTO.getOverallRating());
        feedback.setCleanliness(feedbackRequestDTO.getCleanliness());
        feedback.setPunctuality(feedbackRequestDTO.getPunctuality());
        feedback.setStaffBehavior(feedbackRequestDTO.getStaffBehavior());
        feedback.setComfort(feedbackRequestDTO.getComfort());
        feedback.setComments(feedbackRequestDTO.getComments());
        feedback.setCategory("GENERAL"); // Default category
        feedback.setBusName(feedbackRequestDTO.getBusName());
        feedback.setJourneyDate(feedbackRequestDTO.getJourneyDate());
        feedback.setActive(true);
        
        FeedBack savedFeedback = feedbackDao.save(feedback);
        return convertToResponseDTO(savedFeedback);
    }
    
    @Override
    public FeedbackResponseDTO getEnhancedFeedbackById(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));
        return convertToResponseDTO(feedback);
    }
    
    @Override
    public List<FeedbackResponseDTO> getAllEnhancedFeedback() {
        return feedbackDao.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackResponseDTO> getEnhancedFeedbackByUserId(Long userId) {
        return feedbackDao.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackResponseDTO> getEnhancedFeedbackByBookingId(Long bookingId) {
        return feedbackDao.findByBookingId(bookingId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackResponseDTO> getEnhancedFeedbackByBusName(String busName) {
        return feedbackDao.findByBusName(busName)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackResponseDTO> getEnhancedFeedbackByCategory(String category) {
        return feedbackDao.findByCategory(category)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FeedbackResponseDTO updateEnhancedFeedback(Long id, FeedbackRequestDTO feedbackRequestDTO) {
        validateFeedbackRequest(feedbackRequestDTO);
        
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));
        
        if (!feedback.isActive()) {
            throw new IllegalStateException("Cannot update inactive feedback");
        }
        
        // Update feedback fields
        feedback.setRating(feedbackRequestDTO.getOverallRating());
        feedback.setOverallExperience(feedbackRequestDTO.getOverallRating());
        feedback.setCleanliness(feedbackRequestDTO.getCleanliness());
        feedback.setPunctuality(feedbackRequestDTO.getPunctuality());
        feedback.setStaffBehavior(feedbackRequestDTO.getStaffBehavior());
        feedback.setComfort(feedbackRequestDTO.getComfort());
        feedback.setComments(feedbackRequestDTO.getComments());
        feedback.setBusName(feedbackRequestDTO.getBusName());
        feedback.setJourneyDate(feedbackRequestDTO.getJourneyDate());
        
        FeedBack updatedFeedback = feedbackDao.save(feedback);
        return convertToResponseDTO(updatedFeedback);
    }
    
    @Override
    @Transactional
    public void deleteEnhancedFeedback(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID: " + id));
        
        feedback.setActive(false);
        feedbackDao.save(feedback);
    }
    
    // Legacy Methods (for backward compatibility)
    
    @Override
    @Transactional
    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        User user = userDao.findById(feedbackDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Booking booking = bookingDao.findById(feedbackDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        FeedBack feedback = new FeedBack();
        feedback.setUser(user);
        feedback.setBooking(booking);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCleanliness(feedbackDTO.getCleanliness());
        feedback.setPunctuality(feedbackDTO.getPunctuality());
        feedback.setStaffBehavior(feedbackDTO.getStaffBehavior());
        feedback.setComfort(feedbackDTO.getComfort());
        feedback.setOverallExperience(feedbackDTO.getOverallExperience());
        feedback.setComments(feedbackDTO.getComments());
        feedback.setCategory(feedbackDTO.getCategory());
        feedback.setBusName(feedbackDTO.getBusName());
        feedback.setJourneyDate(feedbackDTO.getJourneyDate());
        feedback.setActive(true);
        
        FeedBack savedFeedback = feedbackDao.save(feedback);
        return convertToDTO(savedFeedback);
    }
    
    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        return convertToDTO(feedback);
    }
    
    @Override
    public List<FeedbackDTO> getAllFeedback() {
        return feedbackDao.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackDTO> getFeedbackByUserId(Long userId) {
        return feedbackDao.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackDTO> getFeedbackByBookingId(Long bookingId) {
        return feedbackDao.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackDTO> getFeedbackByBusName(String busName) {
        return feedbackDao.findByBusName(busName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackDTO> getFeedbackByCategory(String category) {
        return feedbackDao.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FeedbackDTO updateFeedback(Long id, FeedbackDTO feedbackDTO) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCleanliness(feedbackDTO.getCleanliness());
        feedback.setPunctuality(feedbackDTO.getPunctuality());
        feedback.setStaffBehavior(feedbackDTO.getStaffBehavior());
        feedback.setComfort(feedbackDTO.getComfort());
        feedback.setOverallExperience(feedbackDTO.getOverallExperience());
        feedback.setComments(feedbackDTO.getComments());
        feedback.setCategory(feedbackDTO.getCategory());
        feedback.setBusName(feedbackDTO.getBusName());
        feedback.setJourneyDate(feedbackDTO.getJourneyDate());
        
        FeedBack updatedFeedback = feedbackDao.save(feedback);
        return convertToDTO(updatedFeedback);
    }
    
    @Override
    @Transactional
    public void deleteFeedback(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        
        feedback.setActive(false);
        feedbackDao.save(feedback);
    }
    
    // Helper Methods
    
    private void validateFeedbackRequest(FeedbackRequestDTO feedbackRequestDTO) {
        if (feedbackRequestDTO == null) {
            throw new IllegalArgumentException("Feedback request cannot be null");
        }
        
        if (feedbackRequestDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        if (feedbackRequestDTO.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        
        if (feedbackRequestDTO.getOverallRating() == null || feedbackRequestDTO.getOverallRating() < 1 || feedbackRequestDTO.getOverallRating() > 5) {
            throw new IllegalArgumentException("Overall rating must be between 1 and 5");
        }
        
        // Validate individual category ratings if provided
        if (feedbackRequestDTO.getCleanliness() != null && (feedbackRequestDTO.getCleanliness() < 1 || feedbackRequestDTO.getCleanliness() > 5)) {
            throw new IllegalArgumentException("Cleanliness rating must be between 1 and 5");
        }
        
        if (feedbackRequestDTO.getPunctuality() != null && (feedbackRequestDTO.getPunctuality() < 1 || feedbackRequestDTO.getPunctuality() > 5)) {
            throw new IllegalArgumentException("Punctuality rating must be between 1 and 5");
        }
        
        if (feedbackRequestDTO.getStaffBehavior() != null && (feedbackRequestDTO.getStaffBehavior() < 1 || feedbackRequestDTO.getStaffBehavior() > 5)) {
            throw new IllegalArgumentException("Staff behavior rating must be between 1 and 5");
        }
        
        if (feedbackRequestDTO.getComfort() != null && (feedbackRequestDTO.getComfort() < 1 || feedbackRequestDTO.getComfort() > 5)) {
            throw new IllegalArgumentException("Comfort rating must be between 1 and 5");
        }
        
        if (feedbackRequestDTO.getComments() == null || feedbackRequestDTO.getComments().trim().isEmpty()) {
            throw new IllegalArgumentException("Comments cannot be empty");
        }
        
        if (feedbackRequestDTO.getComments().length() < 10) {
            throw new IllegalArgumentException("Comments must be at least 10 characters long");
        }
        
        if (feedbackRequestDTO.getComments().length() > 1000) {
            throw new IllegalArgumentException("Comments cannot exceed 1000 characters");
        }
        
        // Set default values for optional fields if they are null
        if (feedbackRequestDTO.getBusName() == null || feedbackRequestDTO.getBusName().trim().isEmpty()) {
            feedbackRequestDTO.setBusName("Unknown Bus");
        }
        
        if (feedbackRequestDTO.getJourneyDate() == null || feedbackRequestDTO.getJourneyDate().trim().isEmpty()) {
            feedbackRequestDTO.setJourneyDate("Unknown Date");
        }
    }
    
    private FeedbackResponseDTO convertToResponseDTO(FeedBack feedback) {
        String userName = feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName();
        Integer overallRating = feedback.getRating() != null ? feedback.getRating() : feedback.getOverallExperience();
        
        return new FeedbackResponseDTO(
            feedback.getId(),
            feedback.getUser().getId(),
            userName,
            feedback.getBooking().getId(),
            overallRating,
            feedback.getCleanliness(),
            feedback.getPunctuality(),
            feedback.getStaffBehavior(),
            feedback.getComfort(),
            feedback.getComments(),
            feedback.getBusName(),
            feedback.getJourneyDate(),
            feedback.getCategory() != null ? feedback.getCategory() : "GENERAL",
            feedback.getCreatedAt(),
            feedback.isActive()
        );
    }
    
    private FeedbackDTO convertToDTO(FeedBack feedback) {
        return new FeedbackDTO(
            feedback.getId(),
            feedback.getUser().getId(),
            feedback.getBooking().getId(),
            feedback.getRating(),
            feedback.getCleanliness(),
            feedback.getPunctuality(),
            feedback.getStaffBehavior(),
            feedback.getComfort(),
            feedback.getOverallExperience(),
            feedback.getComments(),
            feedback.getCategory(),
            feedback.getBusName(),
            feedback.getJourneyDate()
        );
    }
    
    @Override
    public Map<String, Object> getFeedbackStatistics() {
        List<FeedBack> allFeedback = feedbackDao.findByIsActiveTrue();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFeedback", allFeedback.size());
        
        if (!allFeedback.isEmpty()) {
            double avgOverallRating = allFeedback.stream()
                .mapToInt(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0))
                .filter(rating -> rating > 0)
                .average()
                .orElse(0.0);
            stats.put("averageOverallRating", Math.round(avgOverallRating * 100.0) / 100.0);
            
            // Category distribution
            Map<String, Long> categoryCount = allFeedback.stream()
                .collect(Collectors.groupingBy(f -> f.getCategory() != null ? f.getCategory() : "GENERAL", Collectors.counting()));
            stats.put("categoryDistribution", categoryCount);
            
            // Rating distribution
            Map<Integer, Long> ratingCount = allFeedback.stream()
                .collect(Collectors.groupingBy(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0), Collectors.counting()));
            stats.put("ratingDistribution", ratingCount);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getFeedbackStatisticsByBus(String busName) {
        List<FeedBack> busFeedback = feedbackDao.findByBusName(busName);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("busName", busName);
        stats.put("totalFeedback", busFeedback.size());
        
        if (!busFeedback.isEmpty()) {
            double avgOverallRating = busFeedback.stream()
                .mapToInt(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0))
                .filter(rating -> rating > 0)
                .average()
                .orElse(0.0);
            stats.put("averageOverallRating", Math.round(avgOverallRating * 100.0) / 100.0);
            
            // Category distribution for this bus
            Map<String, Long> categoryCount = busFeedback.stream()
                .collect(Collectors.groupingBy(f -> f.getCategory() != null ? f.getCategory() : "GENERAL", Collectors.counting()));
            stats.put("categoryDistribution", categoryCount);
        }
        
        return stats;
    }
} 