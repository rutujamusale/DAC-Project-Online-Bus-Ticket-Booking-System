package com.bus_ticket.dto.Feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Feedback response data transfer object")
public class FeedbackResponseDTO {
    
    @Schema(description = "Feedback ID", example = "1")
    private Long id;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "User name", example = "John Doe")
    private String userName;
    
    @Schema(description = "Booking ID", example = "1")
    private Long bookingId;
    
    @Schema(description = "Overall rating (1-5)", example = "5")
    private Integer overallRating;
    
    @Schema(description = "Overall rating stars (for display)", example = "★★★★★")
    private String overallRatingStars;
    
    @Schema(description = "Cleanliness rating (1-5)", example = "5")
    private Integer cleanliness;
    
    @Schema(description = "Cleanliness rating stars", example = "★★★★★")
    private String cleanlinessStars;
    
    @Schema(description = "Punctuality rating (1-5)", example = "5")
    private Integer punctuality;
    
    @Schema(description = "Punctuality rating stars", example = "★★★★★")
    private String punctualityStars;
    
    @Schema(description = "Staff behavior rating (1-5)", example = "5")
    private Integer staffBehavior;
    
    @Schema(description = "Staff behavior rating stars", example = "★★★★★")
    private String staffBehaviorStars;
    
    @Schema(description = "Comfort rating (1-5)", example = "5")
    private Integer comfort;
    
    @Schema(description = "Comfort rating stars", example = "★★★★★")
    private String comfortStars;
    
    @Schema(description = "Feedback comments", example = "Great journey experience! The bus was clean and the staff was very helpful.")
    private String comments;
    
    @Schema(description = "Bus name", example = "Express Bus")
    private String busName;
    
    @Schema(description = "Journey date", example = "2024-01-15")
    private String journeyDate;
    
    @Schema(description = "Feedback creation date")
    private LocalDateTime createdAt;
    
    @Schema(description = "Feedback category", example = "GENERAL")
    private String category;
    
    @Schema(description = "Feedback status", example = "true")
    private boolean isActive;
    
    // Constructor for easy conversion
    public FeedbackResponseDTO(Long id, Long userId, String userName, Long bookingId, 
                              Integer overallRating, Integer cleanliness, Integer punctuality,
                              Integer staffBehavior, Integer comfort, String comments, 
                              String busName, String journeyDate, String category, LocalDateTime createdAt, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.bookingId = bookingId;
        this.overallRating = overallRating;
        this.overallRatingStars = generateRatingStars(overallRating);
        this.cleanliness = cleanliness;
        this.cleanlinessStars = generateRatingStars(cleanliness);
        this.punctuality = punctuality;
        this.punctualityStars = generateRatingStars(punctuality);
        this.staffBehavior = staffBehavior;
        this.staffBehaviorStars = generateRatingStars(staffBehavior);
        this.comfort = comfort;
        this.comfortStars = generateRatingStars(comfort);
        this.comments = comments;
        this.busName = busName;
        this.journeyDate = journeyDate;
        this.category = category;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }
    
    // Helper method to generate star rating display
    private String generateRatingStars(Integer rating) {
        if (rating == null) return "Not rated";
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }
} 