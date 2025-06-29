package com.partizip.community.entity;

import com.partizip.community.observer.CommunityObserver;
import com.partizip.community.observer.CommunityEvent;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "polls")
@EntityListeners(AuditingEntityListener.class)
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID pollID;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID authorID;

    @Column(nullable = false, length = 500)
    private String question;

    @ElementCollection
    @CollectionTable(name = "poll_options", joinColumns = @JoinColumn(name = "poll_id"))
    @MapKeyColumn(name = "option_name")
    @Column(name = "vote_count")
    private Map<String, Integer> options = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "poll_votes", joinColumns = @JoinColumn(name = "poll_id"))
    @MapKeyColumn(name = "user_id", columnDefinition = "BINARY(16)")
    @Column(name = "selected_option")
    private Map<UUID, String> votes = new HashMap<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Transient
    private Set<CommunityObserver> observers = new HashSet<>();

    // Constructors
    public Poll() {}

    public Poll(UUID authorID, String question) {
        this.authorID = authorID;
        this.question = question;
    }

    public Poll(UUID authorID, String question, Map<String, Integer> options) {
        this.authorID = authorID;
        this.question = question;
        this.options = new HashMap<>(options);
    }

    // Business methods
    public boolean vote(UUID userID, String option) {
        if (!options.containsKey(option)) {
            throw new IllegalArgumentException("Invalid option: " + option);
        }

        // Check if user already voted
        if (votes.containsKey(userID)) {
            // Remove previous vote
            String previousOption = votes.get(userID);
            options.put(previousOption, options.get(previousOption) - 1);
        }

        // Add new vote
        votes.put(userID, option);
        options.put(option, options.get(option) + 1);

        notifyObservers(new CommunityEvent("POLL_VOTED", this.pollID, userID, option));
        return true;
    }

    public void addOption(String option) {
        options.put(option, 0);
    }

    public boolean hasUserVoted(UUID userID) {
        return votes.containsKey(userID);
    }

    public String getUserVote(UUID userID) {
        return votes.get(userID);
    }

    public int getTotalVotes() {
        return votes.size();
    }

    public Map<String, Double> getVotePercentages() {
        int total = getTotalVotes();
        if (total == 0) {
            return new HashMap<>();
        }

        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String, Integer> entry : options.entrySet()) {
            percentages.put(entry.getKey(), (entry.getValue() / (double) total) * 100);
        }
        return percentages;
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
    public UUID getPollID() {
        return pollID;
    }

    public void setPollID(UUID pollID) {
        this.pollID = pollID;
    }

    public UUID getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UUID authorID) {
        this.authorID = authorID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Integer> getOptions() {
        return new HashMap<>(options);
    }

    public void setOptions(Map<String, Integer> options) {
        this.options = new HashMap<>(options);
    }

    public Map<UUID, String> getVotes() {
        return new HashMap<>(votes);
    }

    public void setVotes(Map<UUID, String> votes) {
        this.votes = new HashMap<>(votes);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "pollID=" + pollID +
                ", authorID=" + authorID +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", totalVotes=" + getTotalVotes() +
                ", createdAt=" + createdAt +
                '}';
    }
}
