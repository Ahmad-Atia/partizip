package com.partizip.gateway.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Reactive WebSocket Handler für Event-Benachrichtigungen
 * WebFlux-kompatible Version für Spring Cloud Gateway
 */
@Component
public class ReactiveEventWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveEventWebSocketHandler.class);
    
    // Sink für Broadcasting von Nachrichten an alle Clients
    private final Sinks.Many<String> messageSink = Sinks.many().multicast().onBackpressureBuffer();
    
    // Session-Tracking für Debugging
    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        String clientInfo = getClientInfo(session);
        activeSessions.put(sessionId, clientInfo);
        
        logger.info("✅ Reactive WebSocket Client verbunden: {} (Total: {})", clientInfo, activeSessions.size());
        
        // Sende Willkommensnachricht
        String welcomeMessage = "WEBSOCKET_CONNECTED|" + sessionId + "|" + System.currentTimeMillis();
        session.send(Mono.just(session.textMessage(welcomeMessage))).subscribe();
        
        // Handle eingehende Nachrichten (Ping/Pong)
        Mono<Void> input = session.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .doOnNext(message -> handleIncomingMessage(session, message))
            .then();
        
        // Handle ausgehende Nachrichten (Broadcast)
        Mono<Void> output = session.send(
            messageSink.asFlux()
                .map(session::textMessage)
                .doOnError(error -> logger.error("❌ Fehler beim Senden an Session {}: {}", sessionId, error.getMessage()))
        );
        
        // Cleanup bei Disconnect
        return Mono.zip(input, output)
            .doFinally(signalType -> {
                activeSessions.remove(sessionId);
                logger.info("❌ Reactive WebSocket Client getrennt: {} (Total: {}) - Signal: {}", 
                           clientInfo, activeSessions.size(), signalType);
            })
            .then();
    }

    /**
     * Behandelt eingehende WebSocket-Nachrichten
     */
    private void handleIncomingMessage(WebSocketSession session, String message) {
        String sessionId = session.getId();
        logger.debug("📨 Nachricht von Client {}: {}", sessionId, message);
        
        // Einfache Ping-Pong für Keep-Alive
        if ("PING".equals(message)) {
            String pongMessage = "PONG|" + System.currentTimeMillis();
            session.send(Mono.just(session.textMessage(pongMessage))).subscribe();
        }
    }

    /**
     * Sendet eine MQTT-Nachricht an alle verbundenen WebSocket-Clients
     */
    public void broadcastMqttMessage(String topic, String mqttPayload) {
        if (activeSessions.isEmpty()) {
            logger.debug("📡 Keine WebSocket-Clients verbunden für Nachricht: [{}] {}", topic, mqttPayload);
            return;
        }

        // Erstelle WebSocket-Nachricht im einfachen Format
        String websocketMessage = "MQTT_EVENT|" + topic + "|" + mqttPayload + "|" + System.currentTimeMillis();
        
        logger.info("📡 Sende an {} Reactive WebSocket-Clients: [{}] {}", activeSessions.size(), topic, mqttPayload);
        
        // Sende über den Sink an alle verbundenen Clients
        Sinks.EmitResult result = messageSink.tryEmitNext(websocketMessage);
        if (result.isFailure()) {
            logger.warn("⚠️ Fehler beim Broadcasting: {}", result);
        }
    }

    /**
     * Extrahiert Client-Informationen für Logging
     */
    private String getClientInfo(WebSocketSession session) {
        String remoteAddress = session.getHandshakeInfo().getRemoteAddress() != null ? 
                              session.getHandshakeInfo().getRemoteAddress().toString() : "unknown";
        return String.format("%s (%s)", session.getId(), remoteAddress);
    }

    /**
     * Gibt die Anzahl der verbundenen Clients zurück
     */
    public int getConnectedClientCount() {
        return activeSessions.size();
    }
}
