package com.partizip.gateway.mqtt;

import com.partizip.gateway.websocket.ReactiveEventWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * Handler der MQTT-Nachrichten empfängt und sie an Reactive WebSocket-Clients weiterleitet
 * Verwendet kein Jackson - arbeitet nur mit String-Nachrichten
 */
@Component
public class MqttToWebSocketHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttToWebSocketHandler.class);

    @Autowired
    private ReactiveEventWebSocketHandler webSocketHandler;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            // Extrahiere Topic und Payload
            String topic = extractTopic(message);
            String payload = extractPayload(message);
            
            logger.info("📥 MQTT-Nachricht empfangen: [{}] {}", topic, payload);
            
            // Leite an WebSocket-Clients weiter
            webSocketHandler.broadcastMqttMessage(topic, payload);
            
        } catch (Exception e) {
            logger.error("🚨 Fehler beim Verarbeiten der MQTT-Nachricht: {}", e.getMessage(), e);
        }
    }

    /**
     * Extrahiert das Topic aus der MQTT-Nachricht
     */
    private String extractTopic(Message<?> message) {
        Object topicHeader = message.getHeaders().get("mqtt_receivedTopic");
        return topicHeader != null ? topicHeader.toString() : "unknown";
    }

    /**
     * Extrahiert den Payload als String aus der MQTT-Nachricht
     */
    private String extractPayload(Message<?> message) {
        Object payload = message.getPayload();
        
        if (payload instanceof String) {
            return (String) payload;
        } else if (payload instanceof byte[]) {
            return new String((byte[]) payload);
        } else {
            return payload.toString();
        }
    }
}
