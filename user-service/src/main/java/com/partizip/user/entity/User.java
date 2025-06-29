package com.partizip.user.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userID;
    
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Lastname is required")
    @Size(max = 100)
    @Column(name = "lastname", nullable = false)
    private String lastname;
    
    @Column(name = "avatar")
    private String avatar;
    
    @JsonIgnore  // Never expose password in JSON responses
    @Column(name = "password_hashed", nullable = false)
    private String passwordHashed;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "address")
    private String address;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    
    // Replace @ElementCollection with @OneToMany
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore  // Prevent infinite recursion in JSON
    private List<UserInterest> userInterests = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore  // Prevent infinite recursion in JSON
    private List<UserParticipation> userParticipations = new ArrayList<>();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Constructors
    public User() {
        this.userID = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public User(String name, String lastname, String email, String passwordHashed) {
        this();
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.passwordHashed = passwordHashed;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    // Helper methods for interests (backwards compatibility)
    public List<String> getInterests() {
        return userInterests.stream()
                .map(UserInterest::getInterest)
                .collect(Collectors.toList());
    }
    
    public void setInterests(List<String> interests) {
        // Clear existing interests
        this.userInterests.clear();
        
        // Add new interests
        if (interests != null) {
            for (String interest : interests) {
                UserInterest userInterest = new UserInterest(this, interest);
                this.userInterests.add(userInterest);
            }
        }
        this.updatedAt = new Date();
    }
    
    public void addInterest(String interest) {
        UserInterest userInterest = new UserInterest(this, interest);
        this.userInterests.add(userInterest);
        this.updatedAt = new Date();
    }
    
    // Helper methods for participation (backwards compatibility)
    public List<String> getParticipation() {
        return userParticipations.stream()
                .map(UserParticipation::getParticipation)
                .collect(Collectors.toList());
    }
    
    public void setParticipation(List<String> participations) {
        // Clear existing participations
        this.userParticipations.clear();
        
        // Add new participations
        if (participations != null) {
            for (String participation : participations) {
                UserParticipation userParticipation = new UserParticipation(this, participation);
                this.userParticipations.add(userParticipation);
            }
        }
        this.updatedAt = new Date();
    }
    
    public void setParti(String parti) {
        UserParticipation userParticipation = new UserParticipation(this, parti);
        this.userParticipations.add(userParticipation);
        this.updatedAt = new Date();
    }

    public List<String> getAllParti() {
        return getParticipation();
    }

    // Profile method - returns user without sensitive data
    public User getProfile() {
        User profile = new User();
        profile.userID = this.userID;
        profile.name = this.name;
        profile.lastname = this.lastname;
        profile.avatar = this.avatar;
        profile.email = this.email;
        profile.address = this.address;
        profile.dateOfBirth = this.dateOfBirth;
        profile.createdAt = this.createdAt;
        profile.updatedAt = this.updatedAt;
        
        // Copy interests and participations as simple lists
        profile.setInterests(this.getInterests());
        profile.setParticipation(this.getParticipation());
        
        // Note: passwordHashed is NOT included
        return profile;
    }

    // Getters and Setters (keeping existing ones)
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        this.updatedAt = new Date();
    }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { 
        this.lastname = lastname;
        this.updatedAt = new Date();
    }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { 
        this.avatar = avatar;
        this.updatedAt = new Date();
    }

    public String getPasswordHashed() { return passwordHashed; }
    public void setPasswordHashed(String passwordHashed) { 
        this.passwordHashed = passwordHashed;
        this.updatedAt = new Date();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email;
        this.updatedAt = new Date();
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { 
        this.address = address;
        this.updatedAt = new Date();
    }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { 
        this.dateOfBirth = dateOfBirth;
        this.updatedAt = new Date();
    }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    // Direct access to entity relationships (for advanced use)
    public List<UserInterest> getUserInterests() { return userInterests; }
    public void setUserInterests(List<UserInterest> userInterests) { this.userInterests = userInterests; }
    
    public List<UserParticipation> getUserParticipations() { return userParticipations; }
    public void setUserParticipations(List<UserParticipation> userParticipations) { this.userParticipations = userParticipations; }
}
