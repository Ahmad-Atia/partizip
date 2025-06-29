package com.partizip.community.service;

import com.partizip.community.dto.CreateCommentDTO;
import com.partizip.community.dto.CreatePollDTO;
import com.partizip.community.dto.CreatePostDTO;
import com.partizip.community.dto.VoteDTO;
import com.partizip.community.entity.Comment;
import com.partizip.community.entity.Poll;
import com.partizip.community.entity.Post;
import com.partizip.community.repository.CommentRepository;
import com.partizip.community.repository.PollRepository;
import com.partizip.community.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class CommunityService {

    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    private final PostRepository postRepository;
    private final PollRepository pollRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Autowired
    public CommunityService(PostRepository postRepository,
                          PollRepository pollRepository,
                          CommentRepository commentRepository,
                          NotificationService notificationService) {
        this.postRepository = postRepository;
        this.pollRepository = pollRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }

    // Post operations
    public UUID createPost(CreatePostDTO createPostDTO) {
        logger.info("Creating new post for author: {}", createPostDTO.getAuthorID());
        
        Post post = new Post(createPostDTO.getAuthorID(), createPostDTO.getContent());
        post.setMediaUrl(createPostDTO.getMediaUrl());
        
        if (createPostDTO.getTaggedEventId() != null) {
            post.tagEvent(createPostDTO.getTaggedEventId());
        }
        
        // Add notification observer
        post.addObserver(notificationService);
        
        Post savedPost = postRepository.save(post);
        logger.info("Post created with ID: {}", savedPost.getPostID());
        
        return savedPost.getPostID();
    }

    public Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Post> getPostsByAuthor(UUID authorId) {
        return postRepository.findByAuthorIDOrderByCreatedAtDesc(authorId);
    }

    public boolean likePost(UUID postId, UUID userId) {
        logger.info("User {} liking post {}", userId, postId);
        
        Post post = getPost(postId);
        post.addObserver(notificationService);
        
        boolean added = post.addLike(userId);
        if (added) {
            postRepository.save(post);
            logger.info("Post {} liked by user {}", postId, userId);
        } else {
            logger.info("User {} already liked post {}", userId, postId);
        }
        
        return added;
    }

    public boolean unlikePost(UUID postId, UUID userId) {
        logger.info("User {} unliking post {}", userId, postId);
        
        Post post = getPost(postId);
        post.addObserver(notificationService);
        
        boolean removed = post.removeLike(userId);
        if (removed) {
            postRepository.save(post);
            logger.info("Post {} unliked by user {}", postId, userId);
        } else {
            logger.info("User {} had not liked post {}", userId, postId);
        }
        
        return removed;
    }

    public void sharePost(UUID postId, UUID userId) {
        logger.info("User {} sharing post {}", userId, postId);
        
        Post post = getPost(postId);
        post.addObserver(notificationService);
        post.share(userId);
        
        logger.info("Post {} shared by user {}", postId, userId);
    }

    // Poll operations
    public UUID createPoll(CreatePollDTO createPollDTO) {
        logger.info("Creating new poll for author: {}", createPollDTO.getAuthorID());
        
        // Initialize options with zero votes
        Map<String, Integer> options = new HashMap<>();
        for (String option : createPollDTO.getOptions()) {
            options.put(option, 0);
        }
        
        Poll poll = new Poll(createPollDTO.getAuthorID(), createPollDTO.getQuestion(), options);
        poll.addObserver(notificationService);
        
        Poll savedPoll = pollRepository.save(poll);
        logger.info("Poll created with ID: {}", savedPoll.getPollID());
        
        return savedPoll.getPollID();
    }

    public Poll getPoll(UUID pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found: " + pollId));
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAllByOrderByCreatedAtDesc();
    }

    public boolean vote(UUID pollId, VoteDTO voteDTO) {
        logger.info("User {} voting '{}' on poll {}", voteDTO.getUserID(), voteDTO.getOption(), pollId);
        
        Poll poll = getPoll(pollId);
        poll.addObserver(notificationService);
        
        try {
            boolean voted = poll.vote(voteDTO.getUserID(), voteDTO.getOption());
            pollRepository.save(poll);
            logger.info("Vote recorded for user {} on poll {}", voteDTO.getUserID(), pollId);
            return voted;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid vote attempt: {}", e.getMessage());
            throw e;
        }
    }

    // Comment operations
    public UUID addComment(UUID targetId, CreateCommentDTO createCommentDTO) {
        logger.info("Adding comment to target {} by author {}", targetId, createCommentDTO.getAuthorID());
        
        // Verify target exists (could be Post or Poll)
        verifyTargetExists(targetId);
        
        Comment comment = new Comment(createCommentDTO.getContent(), 
                                    createCommentDTO.getAuthorID(), 
                                    targetId);
        comment.addObserver(notificationService);
        
        Comment savedComment = commentRepository.save(comment);
        
        // Notify after saving
        comment.notifyCommentCreated();
        
        logger.info("Comment created with ID: {}", savedComment.getCommentID());
        
        return savedComment.getCommentID();
    }

    public List<Comment> getCommentsByTarget(UUID targetId) {
        return commentRepository.findByTargetIDOrderByCreatedAtAsc(targetId);
    }

    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
    }

    // Helper methods
    private void verifyTargetExists(UUID targetId) {
        boolean postExists = postRepository.existsById(targetId);
        boolean pollExists = pollRepository.existsById(targetId);
        
        if (!postExists && !pollExists) {
            throw new IllegalArgumentException("Target not found: " + targetId);
        }
    }

    // Statistics and analytics
    public Map<String, Object> getCommunityStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", postRepository.count());
        stats.put("totalPolls", pollRepository.count());
        stats.put("totalComments", commentRepository.count());
        stats.put("notificationSubscriptions", notificationService.getSubscriptionStats());
        
        return stats;
    }

    public List<Post> getPopularPosts(int minLikes) {
        return postRepository.findPopularPosts(minLikes);
    }

    public List<Poll> getPopularPolls(int minVotes) {
        return pollRepository.findPopularPolls(minVotes);
    }
}
