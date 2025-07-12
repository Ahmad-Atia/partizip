package com.partizip.user.factory;

import com.partizip.user.dto.UserDto;
import com.partizip.user.entity.User;

/**
 * Factory interface for creating UserDto objects using Factory Method Pattern.
 * This pattern provides a way to create UserDto instances without specifying exact classes.
 */
public interface UserDtoCreator {
    
    /**
     * Factory method to create UserDto from User entity.
     * @param user The User entity to convert
     * @return UserDto instance
     */
    UserDto factory(User user);
}
