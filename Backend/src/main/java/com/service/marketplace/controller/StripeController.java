package com.service.marketplace.controller;

import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class StripeController {
    private final StripeService stripeService;
    @PostMapping("/createAccount")
    public String createStripeAccount(@RequestBody StripeAccountRequest stripeAccountRequest) {
        return stripeService.createStripeAccount(stripeAccountRequest);
    }
    @PostMapping("/subscription")
    public String subscriptionWithCheckoutPage(@RequestBody Checkout checkout)  {
        return stripeService.subscriptionWithCheckoutPage(checkout);
    }
    @GetMapping("/plan")
    public String getProductPrice(@RequestParam String priceId) {
        return stripeService.getProductPrice(priceId);
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        return stripeService.handleStripeWebhook(payload, sigHeader);
    }
    @PostMapping("/cancel/{stripeId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable String stripeId) {
        return stripeService.cancelSubscription(stripeId);
    }
}
