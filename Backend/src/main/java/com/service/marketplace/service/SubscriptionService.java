package com.service.marketplace.service;

import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {
    String createStripeAccount(StripeAccountRequest stripeAccountRequest);
    String subscriptionWithCheckoutPage(Checkout checkout);
    String getProductPrice(String priceId);
    ResponseEntity<String> handleStripeWebhook(HttpServletRequest request, String payload);
}
