package com.partizip.community.service;

import com.partizip.community.observer.CommunityEvent;
import com.partizip.community.observer.CommunityObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class NotificationService implements CommunityObserver {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    // Topic-based subscriptions
    private final Map<String, Set<String>> topicSubscriptions = new ConcurrentHashMap<>();
    
    // Event type handling
    private final Map<String, NotificationHandler> eventHandlers = new ConcurrentHashMap<>();
    
    public NotificationService() {
        initializeEventHandlers();
    }
    
    private void initializeEventHandlers() {
        eventHandlers.put("POST_LIKED", this::handlePostLiked);
        eventHandlers.put("POST_UNLIKED", this::handlePostUnliked);
        eventHandlers.put("POST_SHARED", this::handlePostShared);
        eventHandlers.put("POST_TAGGED_EVENT", this::handlePostTaggedEvent);
        eventHandlers.put("COMMENT_CREATED", this::handleCommentCreated);
        eventHandlers.put("POLL_VOTED", this::handlePollVoted);
    }
    
    @Override
    public void notify(CommunityEvent event) {
        logger.info("Received community event: {}", event);
        
        NotificationHandler handler = eventHandlers.get(event.getEventType());
        if (handler != null) {
            try {
                handler.handle(event);
            } catch (Exception e) {
                logger.error("Error handling event: {}", event, e);
            }
        } else {
            logger.warn("No handler found for event type: {}", event.getEventType());
        }
    }
    
    public void subscribe(String topic) {
        topicSubscriptions.computeIfAbsent(topic, k -> new CopyOnWriteArraySet<>())
                         .add("subscriber"); // In a real implementation, this would be user/service IDs
        logger.info("Subscribed to topic: {}", topic);
    }
    
    public void unsubscribe(String topic) {
        Set<String> subscribers = topicSubscriptions.get(topic);
        if (subscribers != null) {
            subscribers.clear();
            logger.info("Unsubscribed from topic: {}", topic);
        }
    }
    
    public void onMessageReceived(String payload) {
        logger.info("Message received: {}", payload);
        // Process the message payload
        // In a real implementation, this could trigger further notifications
    }
    
    // Event-specific handlers
    private void handlePostLiked(CommunityEvent event) {
        String message = String.format("Post %s was liked by user %s", 
                                     event.getTargetId(), event.getUserId());
        sendNotification("post.liked", message);
    }
    
    private void handlePostUnliked(CommunityEvent event) {
        String message = String.format("Post %s was unliked by user %s", 
                                     event.getTargetId(), event.getUserId());
        sendNotification("post.unliked", message);
    }
    
    private void handlePostShared(CommunityEvent event) {
        String message = String.format("Post %s was shared by user %s", 
                                     event.getTargetId(), event.getUserId());
        sendNotification("post.shared", message);
    }
    
    private void handlePostTaggedEvent(CommunityEvent event) {
        String message = String.format("Post %s was tagged to an event by user %s", 
                                     event.getTargetId(), event.getUserId());
        sendNotification("post.tagged", message);
    }
    
    private void handleCommentCreated(CommunityEvent event) {
        String message = String.format("New comment on %s by user %s", 
                                     event.getTargetId(), event.getUserId());
        sendNotification("comment.created", message);
    }
    
    private void handlePollVoted(CommunityEvent event) {
        String message = String.format("User %s voted '%s' on poll %s", 
                                     event.getUserId(), event.getPayload(), event.getTargetId());
        sendNotification("poll.voted", message);
    }
    
    private void sendNotification(String topic, String message) {
        logger.info("Sending notification to topic '{}': {}", topic, message);
        
        // In a real implementation, this would:
        // 1. Send push notifications to mobile devices
        // 2. Send emails
        // 3. Update real-time dashboards via WebSocket
        // 4. Send messages to message queues (Kafka, RabbitMQ)
        // 5. Store notifications in database for later retrieval
        
        // For now, we just log and simulate notification
        Set<String> subscribers = topicSubscriptions.get(topic);
        if (subscribers != null && !subscribers.isEmpty()) {
            logger.info("Notification sent to {} subscribers", subscribers.size());
        }
    }
    
    // Get subscription statistics
    public Map<String, Integer> getSubscriptionStats() {
        Map<String, Integer> stats = new ConcurrentHashMap<>();
        topicSubscriptions.forEach((topic, subscribers) -> 
            stats.put(topic, subscribers.size()));
        return stats;
    }
    
    @FunctionalInterface
    private interface NotificationHandler {
        void handle(CommunityEvent event);
    }
}
