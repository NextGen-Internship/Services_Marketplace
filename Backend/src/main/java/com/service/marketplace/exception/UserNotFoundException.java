package com.service.marketplace.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Integer userId) {
        super("User not found with ID: " + userId);
    }

    public UserNotFoundException(String identifier) {
        super("User not found with identifier: " + identifier);
    }
}