package com.partizip.event.entity;

import com.partizip.event.enums.EventStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID creatorID;
    
    @Column(nullable = false)
    private boolean isPublic;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.PLANNED;
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private Set<Participant> participants = new HashSet<>();
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    
    private Set<Feedback> feedbacks = new HashSet<>();
    
    // Constructors
    public Event() {}
    
    public Event(UUID id, String name, String description, LocalDateTime date, String location,
                UUID creatorID, boolean isPublic, EventStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.creatorID = creatorID;
        this.isPublic = isPublic;
        this.status = status;
        this.participants = new HashSet<>();
        this.feedbacks = new HashSet<>();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public UUID getCreatorID() { return creatorID; }
    public void setCreatorID(UUID creatorID) { this.creatorID = creatorID; }
    
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
    
    public Set<Participant> getParticipants() { return participants; }
    public void setParticipants(Set<Participant> participants) { this.participants = participants; }
    
    public Set<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(Set<Feedback> feedbacks) { this.feedbacks = feedbacks; }
    
    // Business Methods
    public void register(UUID userID) {
        Participant participant = new Participant();
        participant.setUserID(userID);
        participant.setEvent(this);
        participant.setRegistrationDate(LocalDateTime.now());
        participant.setStatus(com.partizip.event.enums.ParticipationStatus.REGISTERED);
        this.participants.add(participant);
    }
    
    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }
    
    public void updateStatus(EventStatus status) {
        this.status = status;
    }
}
