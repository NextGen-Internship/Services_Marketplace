package com.service.marketplace.exception;

public class PayloadProcessingException extends RuntimeException {

    public PayloadProcessingException(String message) {
        super(message);
    }

    public PayloadProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
