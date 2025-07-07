package com.partizip.event.observer;

import com.partizip.event.entity.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Subject class that manages event observers
 */
@Component
public class EventSubject {
    
    private List<EventObserver> observers = new ArrayList<>();
    
    /**
     * Add an observer to the list
     * @param observer The observer to add
     */
    public void addObserver(EventObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Remove an observer from the list
     * @param observer The observer to remove
     */
    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notify all observers of a new event creation
     * @param event The newly created event
     */
    public void notifyEventCreated(Event event) {
        for (EventObserver observer : observers) {
            observer.onEventCreated(event);
        }
    }
    
    /**
     * Notify all observers of an event update
     * @param event The updated event
     */
    public void notifyEventUpdated(Event event) {
        for (EventObserver observer : observers) {
            observer.onEventUpdated(event);
        }
    }
    
    /**
     * Notify all observers of an event deletion
     * @param eventId The ID of the deleted event
     */
    public void notifyEventDeleted(UUID eventId) {
        for (EventObserver observer : observers) {
            observer.onEventDeleted(eventId);
        }
    }
}
