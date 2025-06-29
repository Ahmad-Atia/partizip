package com.partizip.community.repository;

import com.partizip.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    
    List<Post> findByAuthorIDOrderByCreatedAtDesc(UUID authorID);
    
    List<Post> findByTaggedEventIdOrderByCreatedAtDesc(UUID eventId);
    
    List<Post> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Post p WHERE p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<Post> findRecentPosts(@Param("since") LocalDateTime since);
    
    @Query("SELECT p FROM Post p WHERE SIZE(p.likeUserIds) >= :minLikes ORDER BY SIZE(p.likeUserIds) DESC")
    List<Post> findPopularPosts(@Param("minLikes") int minLikes);
    
    @Query("SELECT p FROM Post p WHERE :userId MEMBER OF p.likeUserIds ORDER BY p.createdAt DESC")
    List<Post> findPostsLikedByUser(@Param("userId") UUID userId);
}
