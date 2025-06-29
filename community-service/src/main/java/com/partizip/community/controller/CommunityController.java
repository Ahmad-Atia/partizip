package com.partizip.community.controller;

import com.partizip.community.dto.CreateCommentDTO;
import com.partizip.community.dto.CreatePollDTO;
import com.partizip.community.dto.CreatePostDTO;
import com.partizip.community.dto.VoteDTO;
import com.partizip.community.entity.Comment;
import com.partizip.community.entity.Poll;
import com.partizip.community.entity.Post;
import com.partizip.community.service.CommunityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/community")
@Validated
@CrossOrigin(origins = "*")
public class CommunityController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityController.class);

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }



    @GetMapping("/hello")
    public String hello() {
        return "Hello from CommunityService";
    }


    // Post endpoints
    @PostMapping("/posts")
    public ResponseEntity<Map<String, UUID>> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {
        try {
            logger.info("Creating post: {}", createPostDTO);
            UUID postId = communityService.createPost(createPostDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("postId", postId));
        } catch (Exception e) {
            logger.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = communityService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error retrieving posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable UUID postId) {
        try {
            Post post = communityService.getPost(postId);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            logger.warn("Post not found: {}", postId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving post: {}", postId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        try {
            logger.info("User {} attempting to like post {}", userId, postId);
            boolean liked = communityService.likePost(postId, userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "liked", liked,
                "message", liked ? "Post liked successfully" : "Post already liked by user"
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Post not found for like: {}", postId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error liking post: {}", postId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/posts/{postId}/share")
    public ResponseEntity<Map<String, Object>> sharePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        try {
            logger.info("User {} sharing post {}", userId, postId);
            communityService.sharePost(postId, userId);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Post shared successfully"
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Post not found for share: {}", postId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error sharing post: {}", postId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Poll endpoints
    @PostMapping("/polls")
    public ResponseEntity<Map<String, UUID>> createPoll(@Valid @RequestBody CreatePollDTO createPollDTO) {
        try {
            logger.info("Creating poll: {}", createPollDTO);
            UUID pollId = communityService.createPoll(createPollDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("pollId", pollId));
        } catch (Exception e) {
            logger.error("Error creating poll", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/polls")
    public ResponseEntity<List<Poll>> getAllPolls() {
        try {
            List<Poll> polls = communityService.getAllPolls();
            return ResponseEntity.ok(polls);
        } catch (Exception e) {
            logger.error("Error retrieving polls", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/polls/{pollId}/vote")
    public ResponseEntity<Map<String, Object>> vote(@PathVariable UUID pollId, @Valid @RequestBody VoteDTO voteDTO) {
        try {
            logger.info("Vote attempt on poll {} by user {}", pollId, voteDTO.getUserID());
            boolean voted = communityService.vote(pollId, voteDTO);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "voted", voted,
                "message", "Vote recorded successfully"
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid vote attempt: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error recording vote on poll: {}", pollId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Comment endpoints
    @PostMapping("/comments/{targetId}")
    public ResponseEntity<Map<String, UUID>> addComment(@PathVariable UUID targetId, 
                                                       @Valid @RequestBody CreateCommentDTO createCommentDTO) {
        try {
            logger.info("Adding comment to target {} by author {}", targetId, createCommentDTO.getAuthorID());
            UUID commentId = communityService.addComment(targetId, createCommentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("commentId", commentId));
        } catch (IllegalArgumentException e) {
            logger.warn("Target not found for comment: {}", targetId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error adding comment to target: {}", targetId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/comments/{targetId}")
    public ResponseEntity<List<Comment>> getCommentsByTarget(@PathVariable UUID targetId) {
        try {
            List<Comment> comments = communityService.getCommentsByTarget(targetId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            logger.error("Error retrieving comments for target: {}", targetId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "community-service",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
