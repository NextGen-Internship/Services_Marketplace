package com.service.marketplace.exception;

public class StripeServiceException extends RuntimeException {
    private final String errorCode;
    public StripeServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
