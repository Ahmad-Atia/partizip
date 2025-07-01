package com.partizip.event.entity;

import com.partizip.event.interfaces.EventObserver;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventNotification implements EventObserver {
    
    public UUID eventID;
    public String message;
    public LocalDateTime timestamp;
    
    // Constructors
    public EventNotification() {}
    
    public EventNotification(UUID eventID, String message, LocalDateTime timestamp) {
        this.eventID = eventID;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public UUID getEventID() { return eventID; }
    public void setEventID(UUID eventID) { this.eventID = eventID; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public void subscribe(String topic) {
        // Implementation for subscription logic
        System.out.println("Subscribed to topic: " + topic);
    }
    
    public void onMessageReceived(String payload) {
        // Implementation for message handling
        System.out.println("Message received: " + payload);
    }
    
    @Override
    public void notify(EventNotification event) {
        // Implementation for notification logic
        System.out.println("Notification sent: " + event.message);
    }
}
