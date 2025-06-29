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
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID postID;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID authorID;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(length = 500)
    private String mediaUrl;

    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private Set<UUID> likeUserIds = new HashSet<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "BINARY(16)")
    private UUID taggedEventId;

    @Transient
    private Set<CommunityObserver> observers = new HashSet<>();

    // Constructors
    public Post() {}

    public Post(UUID authorID, String content) {
        this.authorID = authorID;
        this.content = content;
    }

    public Post(UUID authorID, String content, String mediaUrl) {
        this.authorID = authorID;
        this.content = content;
        this.mediaUrl = mediaUrl;
    }

    // Business methods
    public boolean addLike(UUID userId) {
        boolean added = likeUserIds.add(userId);
        if (added) {
            notifyObservers(new CommunityEvent("POST_LIKED", this.postID, userId));
        }
        return added;
    }

    public boolean removeLike(UUID userId) {
        boolean removed = likeUserIds.remove(userId);
        if (removed) {
            notifyObservers(new CommunityEvent("POST_UNLIKED", this.postID, userId));
        }
        return removed;
    }

    public void share(UUID userId) {
        notifyObservers(new CommunityEvent("POST_SHARED", this.postID, userId));
    }

    public void tagEvent(UUID eventId) {
        this.taggedEventId = eventId;
        notifyObservers(new CommunityEvent("POST_TAGGED_EVENT", this.postID, this.authorID));
    }

    // Observer pattern methods
    public void addObserver(CommunityObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CommunityObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(CommunityEvent event) {
        for (CommunityObserver observer : observers) {
            observer.notify(event);
        }
    }

    // Getters and Setters
    public UUID getPostID() {
        return postID;
    }

    public void setPostID(UUID postID) {
        this.postID = postID;
    }

    public UUID getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UUID authorID) {
        this.authorID = authorID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Set<UUID> getLikeUserIds() {
        return new HashSet<>(likeUserIds);
    }

    public void setLikeUserIds(Set<UUID> likeUserIds) {
        this.likeUserIds = new HashSet<>(likeUserIds);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getTaggedEventId() {
        return taggedEventId;
    }

    public void setTaggedEventId(UUID taggedEventId) {
        this.taggedEventId = taggedEventId;
    }

    public int getLikeCount() {
        return likeUserIds.size();
    }

    public boolean isLikedBy(UUID userId) {
        return likeUserIds.contains(userId);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postID=" + postID +
                ", authorID=" + authorID +
                ", content='" + content + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", likeCount=" + likeUserIds.size() +
                ", createdAt=" + createdAt +
                ", taggedEventId=" + taggedEventId +
                '}';
    }
}
