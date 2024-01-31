package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.service.SubscriptionService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Gson gson = new Gson();
    @Value("${STRIPE_PRIVATE_KEY}")
    private String stripeApiKey;

    @Override
    public String subscriptionWithCheckoutPage(Checkout checkout) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = new SessionCreateParams.Builder()
                .setSuccessUrl(checkout.getSuccessUrl())
                .setCancelUrl(checkout.getCancelUrl())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(checkout.getPriceId())
                        .build())
                .build();

        try {
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }
}
