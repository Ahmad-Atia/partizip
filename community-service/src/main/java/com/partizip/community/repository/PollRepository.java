package com.partizip.community.repository;

import com.partizip.community.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {
    
    List<Poll> findByAuthorIDOrderByCreatedAtDesc(UUID authorID);
    
    List<Poll> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Poll p WHERE p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<Poll> findRecentPolls(@Param("since") LocalDateTime since);
    
    @Query("SELECT p FROM Poll p WHERE SIZE(p.votes) >= :minVotes ORDER BY SIZE(p.votes) DESC")
    List<Poll> findPopularPolls(@Param("minVotes") int minVotes);
    
    @Query("SELECT p FROM Poll p WHERE :userId IN (SELECT KEY(v) FROM p.votes v) ORDER BY p.createdAt DESC")
    List<Poll> findPollsVotedByUser(@Param("userId") UUID userId);
}
