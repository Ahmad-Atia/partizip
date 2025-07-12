package com.partizip.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.partizip.user.dto.UserDto;
import com.partizip.user.entity.User;
import com.partizip.user.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // UserDto instance for factory method
    private UserDto userDtoFactory = new UserDto();

    public User createUser(CreateUserRequest userData) {
        // Check if email already exists
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new IllegalArgumentException("User with email already exists");
        }

        User user = new User(
            userData.getName(),
            userData.getLastname(),
            userData.getEmail(),
            userData.getPasswordHashed()
        );
        
        user.setDateOfBirth(userData.getDateOfBirth());
        user.setInterests(userData.getInterests());
        user.setAddress(userData.getAddress());

        return userRepository.save(user);
    }

    public boolean updateUser(String userId, UpdateUserRequest userData) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Update fields if provided
        if (userData.getName() != null) user.setName(userData.getName());
        if (userData.getLastname() != null) user.setLastname(userData.getLastname());
        if (userData.getAvatar() != null) user.setAvatar(userData.getAvatar());
        if (userData.getAddress() != null) user.setAddress(userData.getAddress());
        if (userData.getInterests() != null) user.setInterests(userData.getInterests());

        userRepository.save(user);
        return true;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserProfile(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getProfile).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public User authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    public boolean verifyCredentials(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        return user.isPresent();
    }

    // DTO Classes remain the same...
    public static class CreateUserRequest {
        private String name;
        private String lastname;
        private String email;
        private String passwordHashed;
        private Date dateOfBirth;
        private List<String> interests;
        private String address;

        public CreateUserRequest() {
            // Default constructor for JSON deserialization
        }

        // Getters and setters...
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLastname() { return lastname; }
        public void setLastname(String lastname) { this.lastname = lastname; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPasswordHashed() { return passwordHashed; }
        public void setPasswordHashed(String passwordHashed) { this.passwordHashed = passwordHashed; }

        public Date getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public List<String> getInterests() { return interests; }
        public void setInterests(List<String> interests) { this.interests = interests; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class UpdateUserRequest {
        private String name;
        private String lastname;
        private String avatar;
        private String address;
        private List<String> interests;

        public UpdateUserRequest() {
            // Default constructor for JSON deserialization
        }

        // Getters and setters...
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLastname() { return lastname; }
        public void setLastname(String lastname) { this.lastname = lastname; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public List<String> getInterests() { return interests; }
        public void setInterests(List<String> interests) { this.interests = interests; }
    }

    // DTO-based methods for clean API responses
    public UserDto createUserDto(CreateUserRequest userData) {
        User user = createUser(userData);
        return userDtoFactory.factory(user);
    }

    public UserDto getUserDto(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(userDtoFactory::factory).orElse(null);
    }

    public UserDto getUserDtoByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(userDtoFactory::factory).orElse(null);
    }

    public List<UserDto> getAllUsersDto() {
        List<User> users = userRepository.findAll();
        return users.stream()
                   .map(userDtoFactory::factory)
                   .collect(Collectors.toList());
    }

    public List<UserDto> searchUsersByNameDto(String name) {
        List<User> users = userRepository.findByNameContaining(name);
        return users.stream()
                   .map(userDtoFactory::factory)
                   .collect(Collectors.toList());
    }
}