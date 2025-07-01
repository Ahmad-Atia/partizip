package com.partizip.event.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.partizip.event.enums.ParticipationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "participants")
public class Participant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID participantID;
    
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID userID;
    
    @Column(nullable = false)
    private LocalDateTime registrationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    // Constructors
    public Participant() {}
    
    public Participant(UUID participantID, UUID userID, LocalDateTime registrationDate, 
                      ParticipationStatus status, Event event) {
        this.participantID = participantID;
        this.userID = userID;
        this.registrationDate = registrationDate;
        this.status = status;
        this.event = event;
    }
    
    // Getters and Setters
    public UUID getParticipantID() { return participantID; }
    public void setParticipantID(UUID participantID) { this.participantID = participantID; }
    
    public UUID getUserID() { return userID; }
    public void setUserID(UUID userID) { this.userID = userID; }
    
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    
    public ParticipationStatus getStatus() { return status; }
    public void setStatus(ParticipationStatus status) { this.status = status; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    public void cancelParticipation() {
        this.status = ParticipationStatus.CANCELLED;
    }
    
    public void confirmParticipation() {
        this.status = ParticipationStatus.REGISTERED;
    }
}
