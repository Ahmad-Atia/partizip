-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS partizip_db;

-- Use the database
USE partizip_db;

-- Drop existing tables
DROP TABLE IF EXISTS user_participation;
DROP TABLE IF EXISTS user_interests;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    avatar VARCHAR(255),
    password_hashed VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(500),
    date_of_birth DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create user_interests table with ID column
CREATE TABLE user_interests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- This was missing!
    user_id CHAR(36) NOT NULL,
    interest VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_interests_user_id (user_id)
);

-- Create user_participation table with ID column
CREATE TABLE user_participation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- This was missing!
    user_id CHAR(36) NOT NULL,
    participation VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_participation_user_id (user_id)
);

-- Insert sample users
INSERT INTO users (user_id, name, lastname, email, password_hashed, date_of_birth, address, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'John', 'Doe', 'john.doe@example.com', 'hashedpassword123', '1985-03-15', '123 Main St, City', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440001', 'Jane', 'Smith', 'jane.smith@example.com', 'hashedpassword456', '1990-07-22', '456 Oak Ave, Town', NOW(), NOW());

-- Insert sample interests
INSERT INTO user_interests (user_id, interest) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'programming'),
('550e8400-e29b-41d4-a716-446655440000', 'music'),
('550e8400-e29b-41d4-a716-446655440001', 'reading'),
('550e8400-e29b-41d4-a716-446655440001', 'cooking');

-- Insert sample participation
INSERT INTO user_participation (user_id, participation) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'event_001'),
('550e8400-e29b-41d4-a716-446655440001', 'event_002');