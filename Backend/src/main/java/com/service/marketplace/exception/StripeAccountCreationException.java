package com.service.marketplace.exception;

public class StripeAccountCreationException extends RuntimeException {
    public StripeAccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
