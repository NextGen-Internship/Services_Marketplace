package com.service.marketplace.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(Integer reviewId) {
        super("Review not found with ID: " + reviewId);
    }

    public ReviewNotFoundException() {
        super("Review not found!");
    }
}
