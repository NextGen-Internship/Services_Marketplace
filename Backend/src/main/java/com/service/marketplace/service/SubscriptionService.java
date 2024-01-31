package com.service.marketplace.service;

import com.service.marketplace.dto.request.Checkout;

public interface SubscriptionService {
    String subscriptionWithCheckoutPage(Checkout checkout);
}
