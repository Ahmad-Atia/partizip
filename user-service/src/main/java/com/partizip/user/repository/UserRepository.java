package com.partizip.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.partizip.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find user by email and password (for authentication)
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.passwordHashed = :passwordHashed")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("passwordHashed") String passwordHashed);
    
    // Search users by name
    @Query("SELECT DISTINCT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);
    
    // Get all users without eager loading (to avoid duplication issues)
    @Query("SELECT u FROM User u")
    List<User> findAllUsers();
    
    // Find users by interest (if needed)
    @Query("SELECT DISTINCT u FROM User u JOIN u.userInterests ui WHERE ui.interest = :interest")
    List<User> findByInterest(@Param("interest") String interest);
    
    // Find users by participation (if needed)
    @Query("SELECT DISTINCT u FROM User u JOIN u.userParticipations up WHERE up.participation = :participation")
    List<User> findByParticipation(@Param("participation") String participation);
}