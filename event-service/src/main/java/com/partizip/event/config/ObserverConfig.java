package com.partizip.event.config;

import com.partizip.event.observer.EventSubject;
import com.partizip.event.observer.LoggingEventObserver;
import com.partizip.event.mqtt.MqttEventObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Configuration class for event service
 */
@Configuration
public class ObserverConfig {
    
    @Autowired
    private EventSubject eventSubject;
    
    @Autowired
    private LoggingEventObserver loggingEventObserver;
    
    @Autowired(required = false)
    private MqttEventObserver mqttEventObserver;
    
    @EventListener(ApplicationReadyEvent.class)
    public void registerObservers() {
        // Register logging observer
        eventSubject.addObserver(loggingEventObserver);
        
        // Register MQTT observer if available
        if (mqttEventObserver != null) {
            eventSubject.addObserver(mqttEventObserver);
        }
    }
}
