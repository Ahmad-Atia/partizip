package com.partizip.user.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.partizip.user.dto.UserDto;
import com.partizip.user.entity.User;
import com.partizip.user.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Service";
    }

    @GetMapping("/health")
    public String health() {
        return "User Service is running";
    }

    // User registration - Updated with English field names
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            // Validate required fields
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getLastname() == null || request.getLastname().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            UserService.CreateUserRequest createRequest = new UserService.CreateUserRequest();
            
            createRequest.setName(request.getName());
            createRequest.setLastname(request.getLastname());
            createRequest.setEmail(request.getEmail());
            createRequest.setPasswordHashed(request.getPassword()); // In real app, hash this
            createRequest.setAddress(request.getAddress());
            createRequest.setDateOfBirth(request.getDateOfBirth());
            createRequest.setInterests(request.getInterests());

            UserDto userDto = userService.createUserDto(createRequest);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Get user profile
    @GetMapping("/profile/{userID}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userID") String userID) {
        UserDto userDto = userService.getUserDto(userID);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    // Update user profile
    @PutMapping("/profile/{userID}")
    public ResponseEntity<String> updateUser(@PathVariable("userID") String userID, @RequestBody UserService.UpdateUserRequest request) {
        boolean updated = userService.updateUser(userID, request);
        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersDto());
    }    
    
    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam("name") String name) {
        return ResponseEntity.ok(userService.searchUsersByNameDto(name));
    }

    // Internal endpoint for authentication
    @PostMapping("/internal/verify-credentials")
    public ResponseEntity<CredentialsResponse> verifyCredentials(@RequestBody CredentialsRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if (user != null && user.getPasswordHashed().equals(request.getPassword())) {
            return ResponseEntity.ok(new CredentialsResponse(user.getUserID(), user.getEmail(), true));
        }
        return ResponseEntity.ok(new CredentialsResponse(null, null, false));
    }

    // DTO Classes - Updated with English field names
    public static class RegisterUserRequest {
        private String name;
        private String lastname;
        private String email;
        private String password;
        private String address;
        private Date dateOfBirth;
        private List<String> interests;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLastname() { return lastname; }
        public void setLastname(String lastname) { this.lastname = lastname; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public Date getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public List<String> getInterests() { return interests; }
        public void setInterests(List<String> interests) { this.interests = interests; }
    }

    public static class CredentialsRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class CredentialsResponse {
        private final String userId;
        private final String email;
        private final boolean valid;

        public CredentialsResponse(String userId, String email, boolean valid) {
            this.userId = userId;
            this.email = email;
            this.valid = valid;
        }

        public String getUserId() { return userId; }
        public String getEmail() { return email; }
        public boolean isValid() { return valid; }
    }
}