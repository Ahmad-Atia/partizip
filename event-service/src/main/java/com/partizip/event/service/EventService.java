package com.partizip.event.service;

import com.partizip.event.entity.Event;
import com.partizip.event.entity.Participant;
import com.partizip.event.entity.Feedback;
import com.partizip.event.repository.EventRepository;
import com.partizip.event.repository.ParticipantRepository;
import com.partizip.event.repository.FeedbackRepository;
import com.partizip.event.enums.ParticipationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
    
    public Optional<Event> getEventById(UUID id) {
        return eventRepository.findById(id);
    }
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }
    
    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }
    
    public void registerParticipant(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        Participant participant = new Participant();
        participant.setUserID(userId);
        participant.setEvent(event);
        participant.setRegistrationDate(LocalDateTime.now());
        participant.setStatus(ParticipationStatus.REGISTERED);
        
        participantRepository.save(participant);
    }
    
    public Feedback addFeedback(UUID eventId, Feedback feedback) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        feedback.setEvent(event);
        return feedbackRepository.save(feedback);
    }
}
