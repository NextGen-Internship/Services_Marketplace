package com.service.marketplace.controller;

import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.enums.UserRole;
import com.service.marketplace.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.SubscriptionListParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/createAccount")
    public String createStripeAccount(@RequestBody StripeAccountRequest stripeAccountRequest) throws StripeException {
        return subscriptionService.createStripeAccount(stripeAccountRequest);
    }

    @PostMapping("/subscription")
    public String subscriptionWithCheckoutPage(@RequestBody Checkout checkout) throws StripeException {
        return subscriptionService.subscriptionWithCheckoutPage(checkout);
    }

    @GetMapping("/plan")
    public String getProductPrice(@RequestParam String priceId) {
        return subscriptionService.getProductPrice(priceId);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        return subscriptionService.handleStripeWebhook(payload, sigHeader);
    }
}
