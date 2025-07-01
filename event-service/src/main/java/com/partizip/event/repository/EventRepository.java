package com.partizip.event.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.partizip.event.entity.Event;
import com.partizip.event.enums.EventStatus;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByCreatorID(UUID creatorID);
    List<Event> findByIsPublic(boolean isPublic);
}
