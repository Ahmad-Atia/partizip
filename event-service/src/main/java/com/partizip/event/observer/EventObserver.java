package com.partizip.event.observer;

import com.partizip.event.entity.Event;

/**
 * Observer interface for event notifications
 */
public interface EventObserver {
    
    /**
     * Method called when a new event is created
     * @param event The newly created event
     */
    void onEventCreated(Event event);
    
    /**
     * Method called when an event is updated
     * @param event The updated event
     */
    void onEventUpdated(Event event);
    
    /**
     * Method called when an event is deleted
     * @param eventId The ID of the deleted event
     */
    void onEventDeleted(java.util.UUID eventId);
}
