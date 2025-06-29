package com.partizip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class CreatePollDTO {

    @NotNull(message = "Author ID cannot be null")
    private UUID authorID;

    @NotBlank(message = "Question cannot be blank")
    @Size(max = 500, message = "Question cannot exceed 500 characters")
    private String question;

    @NotNull(message = "Options cannot be null")
    @Size(min = 2, message = "Poll must have at least 2 options")
    private List<String> options;

    // Constructors
    public CreatePollDTO() {}

    public CreatePollDTO(UUID authorID, String question, List<String> options) {
        this.authorID = authorID;
        this.question = question;
        this.options = options;
    }

    // Getters and Setters
    public UUID getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UUID authorID) {
        this.authorID = authorID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "CreatePollDTO{" +
                "authorID=" + authorID +
                ", question='" + question + '\'' +
                ", options=" + options +
                '}';
    }
}
