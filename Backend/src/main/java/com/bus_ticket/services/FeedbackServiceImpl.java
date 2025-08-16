package com.bus_ticket.services;

import com.bus_ticket.dao.FeedBackDao;
import com.bus_ticket.dao.UserDao;
import com.bus_ticket.dao.BookingDao;
import com.bus_ticket.dto.FeedBack.FeedBackDTO;
import com.bus_ticket.dto.FeedBack.FeedBackRequestDTO;
import com.bus_ticket.dto.FeedBack.FeedBackResponseDTO;
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
public class FeedBackServiceImpl implements FeedBackService {
    
    @Autowired
    private FeedBackDao feedbackDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BookingDao bookingDao;
    
    // Enhanced FeedBack Methods
    
    @Override
    @Transactional
    public FeedBackResponseDTO createEnhancedFeedBack(FeedBackRequestDTO feedbackRequestDTO) {
        // Validate input
        validateFeedBackRequest(feedbackRequestDTO);
        
        User user = userDao.findById(feedbackRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + feedbackRequestDTO.getUserId()));
        
        Booking booking = bookingDao.findById(feedbackRequestDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + feedbackRequestDTO.getBookingId()));
        
        // Check if feedback already exists for this booking
        List<FeedBack> existingFeedBack = feedbackDao.findByBookingId(feedbackRequestDTO.getBookingId());
        if (!existingFeedBack.isEmpty()) {
            throw new IllegalStateException("FeedBack already exists for booking ID: " + feedbackRequestDTO.getBookingId());
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
        
        FeedBack savedFeedBack = feedbackDao.save(feedback);
        return convertToResponseDTO(savedFeedBack);
    }
    
    @Override
    public FeedBackResponseDTO getEnhancedFeedBackById(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found with ID: " + id));
        return convertToResponseDTO(feedback);
    }
    
    @Override
    public List<FeedBackResponseDTO> getAllEnhancedFeedBack() {
        return feedbackDao.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackResponseDTO> getEnhancedFeedBackByUserId(Long userId) {
        return feedbackDao.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackResponseDTO> getEnhancedFeedBackByBookingId(Long bookingId) {
        return feedbackDao.findByBookingId(bookingId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackResponseDTO> getEnhancedFeedBackByBusName(String busName) {
        return feedbackDao.findByBusName(busName)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackResponseDTO> getEnhancedFeedBackByCategory(String category) {
        return feedbackDao.findByCategory(category)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FeedBackResponseDTO updateEnhancedFeedBack(Long id, FeedBackRequestDTO feedbackRequestDTO) {
        validateFeedBackRequest(feedbackRequestDTO);
        
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found with ID: " + id));
        
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
        
        FeedBack updatedFeedBack = feedbackDao.save(feedback);
        return convertToResponseDTO(updatedFeedBack);
    }
    
    @Override
    @Transactional
    public void deleteEnhancedFeedBack(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found with ID: " + id));
        
        feedback.setActive(false);
        feedbackDao.save(feedback);
    }
    
    // Legacy Methods (for backward compatibility)
    
    @Override
    @Transactional
    public FeedBackDTO createFeedBack(FeedBackDTO feedbackDTO) {
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
        
        FeedBack savedFeedBack = feedbackDao.save(feedback);
        return convertToDTO(savedFeedBack);
    }
    
    @Override
    public FeedBackDTO getFeedBackById(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found"));
        return convertToDTO(feedback);
    }
    
    @Override
    public List<FeedBackDTO> getAllFeedBack() {
        return feedbackDao.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackDTO> getFeedBackByUserId(Long userId) {
        return feedbackDao.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackDTO> getFeedBackByBookingId(Long bookingId) {
        return feedbackDao.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackDTO> getFeedBackByBusName(String busName) {
        return feedbackDao.findByBusName(busName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedBackDTO> getFeedBackByCategory(String category) {
        return feedbackDao.findByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FeedBackDTO updateFeedBack(Long id, FeedBackDTO feedbackDTO) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found"));
        
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
        
        FeedBack updatedFeedBack = feedbackDao.save(feedback);
        return convertToDTO(updatedFeedBack);
    }
    
    @Override
    @Transactional
    public void deleteFeedBack(Long id) {
        FeedBack feedback = feedbackDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found"));
        
        feedback.setActive(false);
        feedbackDao.save(feedback);
    }
    
    // Helper Methods
    
    private void validateFeedBackRequest(FeedBackRequestDTO feedbackRequestDTO) {
        if (feedbackRequestDTO == null) {
            throw new IllegalArgumentException("FeedBack request cannot be null");
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
    
    private FeedBackResponseDTO convertToResponseDTO(FeedBack feedback) {
        String userName = feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName();
        Integer overallRating = feedback.getRating() != null ? feedback.getRating() : feedback.getOverallExperience();
        
        return new FeedBackResponseDTO(
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
    
    private FeedBackDTO convertToDTO(FeedBack feedback) {
        return new FeedBackDTO(
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
    public Map<String, Object> getFeedBackStatistics() {
        List<FeedBack> allFeedBack = feedbackDao.findByIsActiveTrue();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFeedBack", allFeedBack.size());
        
        if (!allFeedBack.isEmpty()) {
            double avgOverallRating = allFeedBack.stream()
                .mapToInt(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0))
                .filter(rating -> rating > 0)
                .average()
                .orElse(0.0);
            stats.put("averageOverallRating", Math.round(avgOverallRating * 100.0) / 100.0);
            
            // Category distribution
            Map<String, Long> categoryCount = allFeedBack.stream()
                .collect(Collectors.groupingBy(f -> f.getCategory() != null ? f.getCategory() : "GENERAL", Collectors.counting()));
            stats.put("categoryDistribution", categoryCount);
            
            // Rating distribution
            Map<Integer, Long> ratingCount = allFeedBack.stream()
                .collect(Collectors.groupingBy(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0), Collectors.counting()));
            stats.put("ratingDistribution", ratingCount);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getFeedBackStatisticsByBus(String busName) {
        List<FeedBack> busFeedBack = feedbackDao.findByBusName(busName);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("busName", busName);
        stats.put("totalFeedBack", busFeedBack.size());
        
        if (!busFeedBack.isEmpty()) {
            double avgOverallRating = busFeedBack.stream()
                .mapToInt(f -> f.getRating() != null ? f.getRating() : (f.getOverallExperience() != null ? f.getOverallExperience() : 0))
                .filter(rating -> rating > 0)
                .average()
                .orElse(0.0);
            stats.put("averageOverallRating", Math.round(avgOverallRating * 100.0) / 100.0);
            
            // Category distribution for this bus
            Map<String, Long> categoryCount = busFeedBack.stream()
                .collect(Collectors.groupingBy(f -> f.getCategory() != null ? f.getCategory() : "GENERAL", Collectors.counting()));
            stats.put("categoryDistribution", categoryCount);
        }
        
        return stats;
    }
} 