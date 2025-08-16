package com.bus_ticket.dto.Feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Feedback data transfer object")
public class FeedbackDTO {
    
    @Schema(description = "Feedback ID", example = "1")
    private Long id;
    
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @Schema(description = "Booking ID", example = "1")
    private Long bookingId;
    
    @Schema(description = "Overall rating", example = "5")
    private Integer rating;
    
    @Schema(description = "Cleanliness rating", example = "5")
    private Integer cleanliness;
    
    @Schema(description = "Punctuality rating", example = "5")
    private Integer punctuality;
    
    @Schema(description = "Staff behavior rating", example = "5")
    private Integer staffBehavior;
    
    @Schema(description = "Comfort rating", example = "5")
    private Integer comfort;
    
    @Schema(description = "Overall experience rating", example = "5")
    private Integer overallExperience;
    
    @Schema(description = "Additional comments", example = "Great journey experience!")
    private String comments;
    
    @Schema(description = "Feedback category", example = "GENERAL")
    private String category;
    
    @Schema(description = "Bus name", example = "Express Bus")
    private String busName;
    
    @Schema(description = "Journey date", example = "2024-01-15")
    private String journeyDate;
} 