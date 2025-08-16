package com.bus_ticket.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
public class FeedBack extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "cleanliness")
    private Integer cleanliness;
    
    @Column(name = "punctuality")
    private Integer punctuality;
    
    @Column(name = "staff_behavior")
    private Integer staffBehavior;
    
    @Column(name = "comfort")
    private Integer comfort;
    
    @Column(name = "overall_experience")
    private Integer overallExperience;
    
    // Helper method to get overall rating
    public Integer getOverallRating() {
        return this.rating != null ? this.rating : this.overallExperience;
    }
    
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
    
    @Column(name = "category")
    private String category = "GENERAL"; // GENERAL, CLEANLINESS, PUNCTUALITY, STAFF_BEHAVIOR, COMFORT, SAFETY, COMPLAINT, SUGGESTION
    
    @Column(name = "bus_name")
    private String busName;
    
    @Column(name = "journey_date")
    private String journeyDate;
    
    @Column(name = "is_active")
    private boolean isActive = true;
} 