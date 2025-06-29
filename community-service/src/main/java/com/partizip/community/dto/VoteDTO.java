package com.partizip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class VoteDTO {

    @NotNull(message = "User ID cannot be null")
    private UUID userID;

    @NotBlank(message = "Option cannot be blank")
    private String option;

    // Constructors
    public VoteDTO() {}

    public VoteDTO(UUID userID, String option) {
        this.userID = userID;
        this.option = option;
    }

    // Getters and Setters
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
                "userID=" + userID +
                ", option='" + option + '\'' +
                '}';
    }
}
