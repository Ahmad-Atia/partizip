package com.partizip.gateway.service;

import com.partizip.gateway.websocket.ReactiveEventWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Legacy MQTT Message Handler - jetzt umgeleitet auf die neue Reactive WebSocket-Integration
 * Behält die API-Kompatibilität bei, verwendet aber die neue MQTT-Integration
 */
@Service
public class MqttMessageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MqttMessageHandler.class);
    
    @Autowired(required = false)
    private ReactiveEventWebSocketHandler webSocketHandler;
    
    public void publish(String topic, String payload) {
        logger.info("📤 Legacy MQTT publish: [{}] {}", topic, payload);
        // Diese Methode ist jetzt deprecated - Event Service publisht direkt
        logger.warn("⚠️  Use Event Service for MQTT publishing instead of API Gateway");
    }
    
    public void subscribe(String topic) {
        logger.info("📥 Legacy MQTT subscribe: {}", topic);
        logger.info("ℹ️  MQTT subscription is now handled automatically by MqttGatewayConfig");
    }
    
    // Legacy compatibility methods - now logs warnings
    public void publishEventNotification(String eventId, String message) {
        logger.warn("⚠️  publishEventNotification is deprecated. Use Event Service directly.");
        publish("partizip/events/" + eventId, message);
    }
    
    public void publishUserNotification(String userId, String message) {
        logger.warn("⚠️  publishUserNotification is deprecated. Use User Service directly.");
        publish("partizip/users/" + userId, message);
    }
    
    public void publishCommunityNotification(String communityId, String message) {
        logger.warn("⚠️  publishCommunityNotification is deprecated. Use Community Service directly.");
        publish("partizip/communities/" + communityId, message);
    }
    
    // Status methods
    public boolean isWebSocketIntegrationActive() {
        return webSocketHandler != null && webSocketHandler.getConnectedClientCount() > 0;
    }
    
    public int getConnectedWebSocketClients() {
        return webSocketHandler != null ? webSocketHandler.getConnectedClientCount() : 0;
    }
}
