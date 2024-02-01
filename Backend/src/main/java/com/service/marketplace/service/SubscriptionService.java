package com.service.marketplace.service;

import com.service.marketplace.dto.request.Checkout;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;

public interface SubscriptionService {
    String subscriptionWithCheckoutPage(Checkout checkout);
    Product getProductWithPrice(String productId, String priceId) throws StripeException;
}
