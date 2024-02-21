package com.service.marketplace.exception;

import com.stripe.exception.StripeException;

public class StripeSessionCreationException extends RuntimeException {
    public StripeSessionCreationException() {
        super("Stripe session failed!");
    }
}
