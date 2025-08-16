package com.bus_ticket.dto.Feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Feedback request data transfer object")
public class FeedbackRequestDTO {
    
    @NotNull(message = "User ID cannot be null")
    @Schema(description = "User ID", example = "1")
    private Long userId;
    
    @NotNull(message = "Booking ID cannot be null")
    @Schema(description = "Booking ID", example = "1")
    private Long bookingId;
    
    @NotNull(message = "Overall rating cannot be null")
    @Min(value = 1, message = "Overall rating must be at least 1")
    @Max(value = 5, message = "Overall rating must be at most 5")
    @Schema(description = "Overall rating (1-5)", example = "5")
    private Integer overallRating;
    
    @Min(value = 1, message = "Cleanliness rating must be at least 1")
    @Max(value = 5, message = "Cleanliness rating must be at most 5")
    @Schema(description = "Cleanliness rating (1-5)", example = "5")
    private Integer cleanliness;
    
    @Min(value = 1, message = "Punctuality rating must be at least 1")
    @Max(value = 5, message = "Punctuality rating must be at most 5")
    @Schema(description = "Punctuality rating (1-5)", example = "5")
    private Integer punctuality;
    
    @Min(value = 1, message = "Staff behavior rating must be at least 1")
    @Max(value = 5, message = "Staff behavior rating must be at most 5")
    @Schema(description = "Staff behavior rating (1-5)", example = "5")
    private Integer staffBehavior;
    
    @Min(value = 1, message = "Comfort rating must be at least 1")
    @Max(value = 5, message = "Comfort rating must be at most 5")
    @Schema(description = "Comfort rating (1-5)", example = "5")
    private Integer comfort;
    
    @NotBlank(message = "Comments cannot be empty")
    @Size(min = 10, max = 1000, message = "Comments must be between 10 and 1000 characters")
    @Schema(description = "Feedback comments", example = "Great journey experience! The bus was clean and the staff was very helpful.")
    private String comments;
    
    @Schema(description = "Bus name (optional)", example = "Express Bus")
    private String busName;
    
    @Schema(description = "Journey date (optional)", example = "2024-01-15")
    private String journeyDate;
} 