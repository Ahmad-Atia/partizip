package com.partizip.community.observer;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommunityEvent {
    private final String eventType;
    private final UUID targetId;
    private final UUID userId;
    private final LocalDateTime timestamp;
    private final String payload;

    public CommunityEvent(String eventType, UUID targetId, UUID userId) {
        this.eventType = eventType;
        this.targetId = targetId;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.payload = null;
    }

    public CommunityEvent(String eventType, UUID targetId, UUID userId, String payload) {
        this.eventType = eventType;
        this.targetId = targetId;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.payload = payload;
    }

    // Getters
    public String getEventType() {
        return eventType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "CommunityEvent{" +
                "eventType='" + eventType + '\'' +
                ", targetId=" + targetId +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                ", payload='" + payload + '\'' +
                '}';
    }
}
