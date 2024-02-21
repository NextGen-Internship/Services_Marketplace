package com.service.marketplace.exception;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(Integer requestId) {
        super("Request not found with ID: " + requestId);
    }

    public RequestNotFoundException() {
        super("Request not found!");
    }
}