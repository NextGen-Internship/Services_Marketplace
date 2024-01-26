package com.service.marketplace.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscribe")
public class SubscriptionController {

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @PostMapping("/create-subscription-checkout-session")
    public ResponseEntity<Map<String, Object>> createSubscriptionCheckoutSession() {
        Stripe.apiKey = stripeSecretKey;

        try {
            String sessionId = createCheckoutSession();
            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private String createCheckoutSession() throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", List.of("card"));
        params.put("line_items", List.of(Map.of("price", "price_1OcUNzI2KDxgMJyoxeNLRi93", "quantity", 1)));
        params.put("mode", "subscription");
        params.put("success_url", "http://localhost:3000/success");
        params.put("cancel_url", "http://localhost:3000/cancel");

        Session session = Session.create(params);
        return session.getId();
    }
}
