package com.partizip.community.repository;

import com.partizip.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    
    List<Comment> findByTargetIDOrderByCreatedAtAsc(UUID targetID);
    
    List<Comment> findByAuthorIDOrderByCreatedAtDesc(UUID authorID);
    
    @Query("SELECT c FROM Comment c WHERE c.targetID = :targetId AND c.createdAt >= :since ORDER BY c.createdAt ASC")
    List<Comment> findRecentCommentsByTarget(@Param("targetId") UUID targetId, @Param("since") LocalDateTime since);
    
    long countByTargetID(UUID targetID);
    
    long countByAuthorID(UUID authorID);
}
