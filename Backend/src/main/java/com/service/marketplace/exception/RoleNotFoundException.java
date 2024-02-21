package com.service.marketplace.exception;

public class RoleNotFoundException extends IllegalArgumentException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
