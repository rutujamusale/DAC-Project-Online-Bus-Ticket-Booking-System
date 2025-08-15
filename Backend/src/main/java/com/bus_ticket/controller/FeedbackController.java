package com.bus_ticket.controller;

import com.bus_ticket.dto.Feedback.FeedbackDTO;
import com.bus_ticket.dto.Feedback.FeedbackRequestDTO;
import com.bus_ticket.dto.Feedback.FeedbackResponseDTO;
import com.bus_ticket.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
@Tag(name = "Feedback Management", description = "APIs for feedback operations")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Enhanced Feedback Endpoints
    
    @PostMapping("/enhanced")
    @Operation(summary = "Create enhanced feedback", description = "Create a new feedback with rating (1-5) and comments")
    public ResponseEntity<?> createEnhancedFeedback(@RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        try {
            FeedbackResponseDTO createdFeedback = feedbackService.createEnhancedFeedback(feedbackRequestDTO);
            return ResponseEntity.ok(createdFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Business logic error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating feedback: " + e.getMessage());
        }
    }

    @GetMapping("/enhanced/{id}")
    @Operation(summary = "Get enhanced feedback by ID", description = "Get enhanced feedback details by ID")
    public ResponseEntity<?> getEnhancedFeedbackById(@PathVariable Long id) {
        try {
            FeedbackResponseDTO feedback = feedbackService.getEnhancedFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/enhanced")
    @Operation(summary = "Get all enhanced feedback", description = "Get all enhanced feedback")
    public ResponseEntity<?> getAllEnhancedFeedback() {
        try {
            List<FeedbackResponseDTO> feedback = feedbackService.getAllEnhancedFeedback();
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving feedback: " + e.getMessage());
        }
    }

    @GetMapping("/enhanced/user/{userId}")
    @Operation(summary = "Get enhanced feedback by user", description = "Get all enhanced feedback for a specific user")
    public ResponseEntity<?> getEnhancedFeedbackByUserId(@PathVariable Long userId) {
        try {
            List<FeedbackResponseDTO> feedback = feedbackService.getEnhancedFeedbackByUserId(userId);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving user feedback: " + e.getMessage());
        }
    }

    @GetMapping("/enhanced/booking/{bookingId}")
    @Operation(summary = "Get enhanced feedback by booking", description = "Get all enhanced feedback for a specific booking")
    public ResponseEntity<?> getEnhancedFeedbackByBookingId(@PathVariable Long bookingId) {
        try {
            List<FeedbackResponseDTO> feedback = feedbackService.getEnhancedFeedbackByBookingId(bookingId);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving booking feedback: " + e.getMessage());
        }
    }

    @GetMapping("/enhanced/bus/{busName}")
    @Operation(summary = "Get enhanced feedback by bus", description = "Get all enhanced feedback for a specific bus")
    public ResponseEntity<?> getEnhancedFeedbackByBusName(@PathVariable String busName) {
        try {
            List<FeedbackResponseDTO> feedback = feedbackService.getEnhancedFeedbackByBusName(busName);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving bus feedback: " + e.getMessage());
        }
    }

    @GetMapping("/enhanced/category/{category}")
    @Operation(summary = "Get enhanced feedback by category", description = "Get all enhanced feedback for a specific category")
    public ResponseEntity<?> getEnhancedFeedbackByCategory(@PathVariable String category) {
        try {
            List<FeedbackResponseDTO> feedback = feedbackService.getEnhancedFeedbackByCategory(category);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving category feedback: " + e.getMessage());
        }
    }

    @PutMapping("/enhanced/{id}")
    @Operation(summary = "Update enhanced feedback", description = "Update enhanced feedback details")
    public ResponseEntity<?> updateEnhancedFeedback(@PathVariable Long id, @RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        try {
            FeedbackResponseDTO updatedFeedback = feedbackService.updateEnhancedFeedback(id, feedbackRequestDTO);
            return ResponseEntity.ok(updatedFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Business logic error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating feedback: " + e.getMessage());
        }
    }

    @DeleteMapping("/enhanced/{id}")
    @Operation(summary = "Delete enhanced feedback", description = "Soft delete an enhanced feedback")
    public ResponseEntity<?> deleteEnhancedFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteEnhancedFeedback(id);
            return ResponseEntity.ok("Enhanced feedback deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting feedback: " + e.getMessage());
        }
    }
    
    @GetMapping("/enhanced/statistics")
    @Operation(summary = "Get feedback statistics", description = "Get overall feedback statistics")
    public ResponseEntity<?> getFeedbackStatistics() {
        try {
            Map<String, Object> stats = feedbackService.getFeedbackStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving feedback statistics: " + e.getMessage());
        }
    }
    
    @GetMapping("/enhanced/statistics/bus/{busName}")
    @Operation(summary = "Get feedback statistics by bus", description = "Get feedback statistics for a specific bus")
    public ResponseEntity<?> getFeedbackStatisticsByBus(@PathVariable String busName) {
        try {
            Map<String, Object> stats = feedbackService.getFeedbackStatisticsByBus(busName);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving bus feedback statistics: " + e.getMessage());
        }
    }

    // Legacy Endpoints (for backward compatibility)
    
    @PostMapping
    @Operation(summary = "Create feedback", description = "Create a new feedback (legacy)")
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            FeedbackDTO createdFeedback = feedbackService.createFeedback(feedbackDTO);
            return ResponseEntity.ok(createdFeedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating feedback: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Get feedback details by ID (legacy)")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        try {
            FeedbackDTO feedback = feedbackService.getFeedbackById(id);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all feedback", description = "Get all feedback (legacy)")
    public ResponseEntity<?> getAllFeedback() {
        try {
            List<FeedbackDTO> feedback = feedbackService.getAllFeedback();
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving feedback: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get feedback by user", description = "Get all feedback for a specific user (legacy)")
    public ResponseEntity<?> getFeedbackByUserId(@PathVariable Long userId) {
        try {
            List<FeedbackDTO> feedback = feedbackService.getFeedbackByUserId(userId);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving user feedback: " + e.getMessage());
        }
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get feedback by booking", description = "Get all feedback for a specific booking (legacy)")
    public ResponseEntity<?> getFeedbackByBookingId(@PathVariable Long bookingId) {
        try {
            List<FeedbackDTO> feedback = feedbackService.getFeedbackByBookingId(bookingId);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving booking feedback: " + e.getMessage());
        }
    }

    @GetMapping("/bus/{busName}")
    @Operation(summary = "Get feedback by bus", description = "Get all feedback for a specific bus (legacy)")
    public ResponseEntity<?> getFeedbackByBusName(@PathVariable String busName) {
        try {
            List<FeedbackDTO> feedback = feedbackService.getFeedbackByBusName(busName);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving bus feedback: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get feedback by category", description = "Get all feedback for a specific category (legacy)")
    public ResponseEntity<?> getFeedbackByCategory(@PathVariable String category) {
        try {
            List<FeedbackDTO> feedback = feedbackService.getFeedbackByCategory(category);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving category feedback: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feedback", description = "Update feedback details (legacy)")
    public ResponseEntity<?> updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO) {
        try {
            FeedbackDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDTO);
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating feedback: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Soft delete a feedback (legacy)")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok("Feedback deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting feedback: " + e.getMessage());
        }
    }
} 