package com.partizip.event.mqtt;

import com.partizip.event.entity.Event;
import com.partizip.event.observer.EventObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * MQTT Observer that sends notifications when events are created, updated, or deleted
 * Includes robust error handling and connection checking
 */
@Component
@ConditionalOnProperty(name = "mqtt.enabled", havingValue = "true", matchIfMissing = false)
public class MqttEventObserver implements EventObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(MqttEventObserver.class);
    
    @Autowired(required = false)
    private MessageChannel mqttOutboundChannel;
    
    @Override
    public void onEventCreated(Event event) {
        try {
            String notification = createSimpleNotification("EVENT_CREATED", event);
            sendNotification("events/created", notification);
            logger.info("MQTT notification sent for event created: {}", event.getId());
        } catch (Exception e) {
            logger.error("Error sending MQTT notification for event creation: {}", e.getMessage());
        }
    }
    
    @Override
    public void onEventUpdated(Event event) {
        try {
            String notification = createSimpleNotification("EVENT_UPDATED", event);
            sendNotification("events/updated", notification);
            logger.info("MQTT notification sent for event updated: {}", event.getId());
        } catch (Exception e) {
            logger.error("Error sending MQTT notification for event update: {}", e.getMessage());
        }
    }
    
    @Override
    public void onEventDeleted(UUID eventId) {
        try {
            String notification = "EVENT_DELETED|" + eventId.toString() + "|" + System.currentTimeMillis();
            sendNotification("events/deleted", notification);
            logger.info("MQTT notification sent for event deleted: {}", eventId);
        } catch (Exception e) {
            logger.error("Error sending MQTT notification for event deletion: {}", e.getMessage());
        }
    }
    
    private String createSimpleNotification(String type, Event event) {
        // Einfaches Format ohne JSON: TYPE|ID|NAME|DATE|LOCATION|PUBLIC|TIMESTAMP
        return String.format("%s|%s|%s|%s|%s|%s|%d",
                type,
                event.getId().toString(),
                event.getName(),
                event.getDate().toString(),
                event.getLocation(),
                event.isPublic(),
                System.currentTimeMillis()
        );
    }
    
    private void sendNotification(String topic, String notification) {
        try {
            // Prüfe ob MQTT-Komponenten verfügbar sind
            if (mqttOutboundChannel == null) {
                logger.warn("MQTT outbound channel not available, notification logged only: [{}] {}", topic, notification);
                return;
            }

            // Sende MQTT-Nachricht - Spring Integration handhabt Verbindungsfehler automatisch
            logger.debug("Attempting to send MQTT notification to topic '{}': {}", topic, notification);
            
            mqttOutboundChannel.send(MessageBuilder.withPayload(notification)
                    .setHeader("mqtt_topic", topic)
                    .setHeader("mqtt_retained", false)
                    .setHeader("mqtt_qos", 0)
                    .build());
                    
            logger.info("MQTT notification sent successfully to topic: {}", topic);

        } catch (Exception e) {
            logger.warn("Failed to send MQTT notification to topic '{}': {} (Error: {})", 
                       topic, notification, e.getMessage());
            // Event wird trotzdem erfolgreich verarbeitet, auch wenn MQTT fehlschlägt
        }
    }
}
