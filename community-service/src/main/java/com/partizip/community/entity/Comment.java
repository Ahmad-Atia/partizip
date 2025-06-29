package com.partizip.community.entity;

import com.partizip.community.observer.CommunityObserver;
import com.partizip.community.observer.CommunityEvent;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID commentID;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID authorID;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID targetID; // Post oder Poll ID

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Transient
    private Set<CommunityObserver> observers = new HashSet<>();

    // Constructors
    public Comment() {}

    public Comment(String content, UUID authorID, UUID targetID) {
        this.content = content;
        this.authorID = authorID;
        this.targetID = targetID;
    }

    // Observer pattern methods
    public void addObserver(CommunityObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CommunityObserver observer) {
        observers.remove(observer);
    }

    public void notifyCommentCreated() {
        notifyObservers(new CommunityEvent("COMMENT_CREATED", this.targetID, this.authorID));
    }

    private void notifyObservers(CommunityEvent event) {
        for (CommunityObserver observer : observers) {
            observer.notify(event);
        }
    }

    // Getters and Setters
    public UUID getCommentID() {
        return commentID;
    }

    public void setCommentID(UUID commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UUID authorID) {
        this.authorID = authorID;
    }

    public UUID getTargetID() {
        return targetID;
    }

    public void setTargetID(UUID targetID) {
        this.targetID = targetID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", content='" + content + '\'' +
                ", authorID=" + authorID +
                ", targetID=" + targetID +
                ", createdAt=" + createdAt +
                '}';
    }
}
