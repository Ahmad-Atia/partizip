package com.partizip.gateway.websocket;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ReactiveEventWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveEventWebSocketHandler.class);

    // Clientspezifische Konfiguration
    private static final int MAX_CLIENT_BUFFER = 256;
    private static final Duration CLIENT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration KEEPALIVE_INTERVAL = Duration.ofSeconds(30);

    // Sink mit Direktübertragung + Backpressure-Vermeidung durch Replay
    private final Sinks.Many<String> messageSink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(MAX_CLIENT_BUFFER, false);

    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public ReactiveEventWebSocketHandler() {
        // KEEPALIVE
        Flux.interval(KEEPALIVE_INTERVAL)
                .map(tick -> "KEEPALIVE|" + System.currentTimeMillis())
                .subscribe(msg -> messageSink.tryEmitNext(msg));
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        String clientInfo = getClientInfo(session);
        activeSessions.put(sessionId, clientInfo);

        logger.info("✅ WebSocket Client verbunden: {} (Total: {})", clientInfo, activeSessions.size());

        // Willkommensnachricht senden
        session.send(Mono.just(session.textMessage("WEBSOCKET_CONNECTED|" + sessionId + "|" + System.currentTimeMillis())))
                .subscribe();

        // Eingehende Nachrichten behandeln
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(msg -> handleIncomingMessage(session, msg))
                .then();

        // Ausgehende Nachrichten streamen
        Mono<Void> output = session.send(
                messageSink.asFlux()
                        .onBackpressureBuffer(MAX_CLIENT_BUFFER)
                        .timeout(CLIENT_TIMEOUT)
                        .map(session::textMessage)
                        .onErrorResume(err -> {
                            logger.warn("⚠️ Fehler beim Senden an {}: {}", sessionId, err.getMessage());
                            return Flux.empty();
                        })
        );

        // Disconnect Cleanup
        return Mono.when(input, output)
                .doFinally(signal -> {
                    activeSessions.remove(sessionId);
                    logger.info("❌ WebSocket Client getrennt: {} (Total: {}) - Signal: {}", clientInfo, activeSessions.size(), signal);
                });
    }

    private void handleIncomingMessage(WebSocketSession session, String message) {
        String sessionId = session.getId();
        logger.debug("📨 Nachricht von {}: {}", sessionId, message);

        if ("PING".equalsIgnoreCase(message)) {
            session.send(Mono.just(session.textMessage("PONG|" + System.currentTimeMillis())))
                    .subscribe();
        }
    }

    public void broadcastMqttMessage(String topic, String mqttPayload) {
        if (activeSessions.isEmpty()) {
            logger.debug("📡 Kein aktiver WebSocket-Client – MQTT-Nachricht ignoriert: [{}] {}", topic, mqttPayload);
            return;
        }

        String msg = "MQTT_EVENT|" + topic + "|" + mqttPayload + "|" + System.currentTimeMillis();
        Sinks.EmitResult result = messageSink.tryEmitNext(msg);

        if (result.isFailure()) {
            logger.warn("⚠️ Fehler beim Broadcasting an {} Clients: {} – {}", activeSessions.size(), topic, result);
        } else {
            logger.info("📡 MQTT an {} Clients gesendet: [{}]", activeSessions.size(), topic);
        }
    }

    private String getClientInfo(WebSocketSession session) {
        var remote = session.getHandshakeInfo().getRemoteAddress();
        return session.getId() + " (" + (remote != null ? remote.toString() : "unknown") + ")";
    }

    public int getConnectedClientCount() {
        return activeSessions.size();
    }
}
