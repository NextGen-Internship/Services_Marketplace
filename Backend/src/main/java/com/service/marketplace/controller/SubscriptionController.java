package com.service.marketplace.controller;

import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscription")
    public String subscriptionWithCheckoutPage(@RequestBody Checkout checkout) throws StripeException {
        return subscriptionService.subscriptionWithCheckoutPage(checkout);
    }

    @GetMapping("/plan")
    public String getProductPrice(@RequestParam String priceId) {
        return subscriptionService.getProductPrice(priceId);
    }
}
