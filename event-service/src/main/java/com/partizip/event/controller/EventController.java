package com.partizip.event.controller;

import com.partizip.event.entity.Event;
import com.partizip.event.entity.Feedback;
import com.partizip.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
@Validated
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Event Service!";
    }

    @GetMapping("/health")
    public String health() {
        return "Event Service is running";
    }
    
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }
    
    @GetMapping("/events/{id}")
    public ResponseEntity<Event> eventDetails(@PathVariable (value = "id") UUID id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @PostMapping("/events/{eventId}/participants")
    public ResponseEntity<Map<String, Object>> manageParticipation(
            @PathVariable(value = "eventId") UUID eventId, 
            @RequestParam(value = "userId") UUID userId) {
        try {
            eventService.registerParticipant(eventId, userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Participation registered successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    @PostMapping("/events/{eventId}/feedback")
    public ResponseEntity<Feedback> addFeedback(
            @PathVariable(value = "eventId") UUID eventId, 
            @RequestBody Feedback feedback) {
        Feedback addedFeedback = eventService.addFeedback(eventId, feedback);
        return ResponseEntity.ok(addedFeedback);
    }
    
    @GetMapping("/events/{eventId}/feedback")
    public ResponseEntity<List<Feedback>> getFeedback(@PathVariable(value = "eventId") UUID eventId) {
        List<Feedback> feedbacks = eventService.getFeedbacksByEventId(eventId);
        return ResponseEntity.ok(feedbacks);
    }
}
