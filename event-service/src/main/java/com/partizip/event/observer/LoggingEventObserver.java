package com.partizip.event.observer;

import com.partizip.event.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Simple logging observer for demonstration purposes
 */
@Component
public class LoggingEventObserver implements EventObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingEventObserver.class);
    
    @Override
    public void onEventCreated(Event event) {
        logger.info("📅 NEW EVENT CREATED: {} at {} in {}", 
                   event.getName(), 
                   event.getDate(), 
                   event.getLocation());
    }
    
    @Override
    public void onEventUpdated(Event event) {
        logger.info("✏️ EVENT UPDATED: {} at {} in {}", 
                   event.getName(), 
                   event.getDate(), 
                   event.getLocation());
    }
    
    @Override
    public void onEventDeleted(UUID eventId) {
        logger.info("🗑️ EVENT DELETED: {}", eventId);
    }
}
