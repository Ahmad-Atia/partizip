package com.partizip.event.repository;

import com.partizip.event.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByEventId(UUID eventId);
    List<Feedback> findByUserID(UUID userID);
    List<Feedback> findByRating(Integer rating);
}
