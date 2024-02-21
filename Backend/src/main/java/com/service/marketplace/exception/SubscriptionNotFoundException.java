package com.service.marketplace.exception;

public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(Integer subscriptionId) {
        super("Subscription not found with ID: " + subscriptionId);
    }
}
