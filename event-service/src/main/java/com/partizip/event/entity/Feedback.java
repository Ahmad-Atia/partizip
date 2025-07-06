package com.partizip.event.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedbacks")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID feedbackID;
    
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID userID;
    
    
    @Column(length = 2000)
    private String content;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Event event;
    
    // Constructors
    public Feedback() {}
    
    public Feedback(UUID feedbackID, UUID userID, String content, Integer rating, 
                   LocalDateTime createdAt, Event event) {
        this.feedbackID = feedbackID;
        this.userID = userID;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.event = event;
    }
    
    // Getters and Setters
    public UUID getFeedbackID() { return feedbackID; }
    public void setFeedbackID(UUID feedbackID) { this.feedbackID = feedbackID; }
    
    public UUID getUserID() { return userID; }
    public void setUserID(UUID userID) { this.userID = userID; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
