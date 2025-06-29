package com.partizip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateCommentDTO {

    @NotNull(message = "Author ID cannot be null")
    private UUID authorID;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String content;

    // Constructors
    public CreateCommentDTO() {}

    public CreateCommentDTO(UUID authorID, String content) {
        this.authorID = authorID;
        this.content = content;
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

    @Override
    public String toString() {
        return "CreateCommentDTO{" +
                "authorID=" + authorID +
                ", content='" + content + '\'' +
                '}';
    }
}
