package com.service.marketplace.exception;

public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(Integer offerId) {
        super("Offer not found with ID: " + offerId);
    }
}