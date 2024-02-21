package com.service.marketplace.exception;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(Integer serviceId) {
        super("Service not found with ID: " + serviceId);
    }

    public ServiceNotFoundException() {
        super("Service not found!");
    }
}
