package com.partizip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreatePostDTO {

    @NotNull(message = "Author ID cannot be null")
    private UUID authorID;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    private String content;

    @Size(max = 500, message = "Media URL cannot exceed 500 characters")
    private String mediaUrl;

    private UUID taggedEventId;

    // Constructors
    public CreatePostDTO() {}

    public CreatePostDTO(UUID authorID, String content) {
        this.authorID = authorID;
        this.content = content;
    }

    public CreatePostDTO(UUID authorID, String content, String mediaUrl) {
        this.authorID = authorID;
        this.content = content;
        this.mediaUrl = mediaUrl;
    }

    // Getters and Setters
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

    public UUID getTaggedEventId() {
        return taggedEventId;
    }

    public void setTaggedEventId(UUID taggedEventId) {
        this.taggedEventId = taggedEventId;
    }

    @Override
    public String toString() {
        return "CreatePostDTO{" +
                "authorID=" + authorID +
                ", content='" + content + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", taggedEventId=" + taggedEventId +
                '}';
    }
}
