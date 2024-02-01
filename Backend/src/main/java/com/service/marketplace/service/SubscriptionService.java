package com.service.marketplace.service;

import com.service.marketplace.dto.request.Checkout;
import com.stripe.exception.StripeException;

public interface SubscriptionService {
    String subscriptionWithCheckoutPage(Checkout checkout);
    String getProductPrice(String priceId);
}
