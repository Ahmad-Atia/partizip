package com.partizip.user.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.partizip.user.factory.UserDtoCreator;

/**
 * Data Transfer Object for User entity.
 * Used for API responses to avoid JSON serialization issues and to hide sensitive data.
 * Implements UserDtoCreator interface for Factory Method Pattern.
 */
public class UserDto implements UserDtoCreator {
    
    private String userID;
    private String name;
    private String lastname;
    private String avatar;
    private String email;
    private String address;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    
    private List<String> interests;
    private List<String> participation;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updatedAt;

    // Default constructor - needed for JSON serialization/deserialization
    public UserDto() {
        // Empty constructor for framework usage
    }

    // Factory method implementation from UserDtoCreator interface
    @Override
    public UserDto factory(com.partizip.user.entity.User user) {
        if (user == null) {
            return null;
        }
        
        UserDto dto = new UserDto();
        dto.userID = user.getUserID();
        dto.name = user.getName();
        dto.lastname = user.getLastname();
        dto.avatar = user.getAvatar();
        dto.email = user.getEmail();
        dto.address = user.getAddress();
        dto.dateOfBirth = user.getDateOfBirth();
        dto.interests = user.getInterests();
        dto.participation = user.getParticipation();
        dto.createdAt = user.getCreatedAt();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getParticipation() {
        return participation;
    }

    public void setParticipation(List<String> participation) {
        this.participation = participation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", interests=" + interests +
                ", participation=" + participation +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
