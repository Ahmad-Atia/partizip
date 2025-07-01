package com.partizip.event.repository;

import com.partizip.event.entity.Participant;
import com.partizip.event.enums.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByUserID(UUID userID);
    List<Participant> findByEventId(UUID eventId);
    List<Participant> findByStatus(ParticipationStatus status);
}
