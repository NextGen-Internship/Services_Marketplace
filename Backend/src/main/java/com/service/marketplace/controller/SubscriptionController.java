package com.service.marketplace.controller;

import com.service.marketplace.dto.request.RequestDTO;
import com.service.marketplace.persistence.entity.SubscriptionDAO;
import com.service.marketplace.util.CustomerUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/subscribe")
public class SubscriptionController {
//    @Value("${stripe.secretKey}")
//    private String stripeSecretKey;
    // String STRIPE_API_KEY = System.getenv().get("STRIPE_API_KEY");

    @PostMapping("/subscriptions/new")
    public String newSubscription(@RequestBody RequestDTO requestDTO) throws StripeException {
        Stripe.apiKey = "sk_test_51OcQX6I2KDxgMJyoLEPzCcdVgucBUKxHjaTYal5aaj0i3z4PzUCktvxT1yjiJKCmOYiqes1OKtzkTvbNWjolFjrm00Tzq3PmyY";

        String clientBaseURL = "http://localhost:3000";

        // Start by finding existing customer record from Stripe or creating a new one if needed
        Customer customer = CustomerUtil.findOrCreateCustomer(requestDTO.getCustomerEmail(), requestDTO.getCustomerName());

        // Next, create a checkout session by adding the details of the checkout
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        // For subscriptions, you need to set the mode as subscription
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customer.getId())
                        .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientBaseURL + "/failure");

        // Assuming only one product in the request
        Product product = requestDTO.getSubscription();

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder() // Using PriceData from Stripe
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .putMetadata("app_id", product.getId())
                                                        .setName(product.getName())
                                                        .build()
                                        )
                                        .setCurrency(SubscriptionDAO.getProduct(product.getId()).getDefaultPriceObject().getCurrency())
                                        .setUnitAmountDecimal(SubscriptionDAO.getProduct(product.getId()).getDefaultPriceObject().getUnitAmountDecimal())
                                        // For subscriptions, you need to provide the details on how often they would recur
                                        .setRecurring(SessionCreateParams.LineItem.PriceData.Recurring.builder().setInterval(SessionCreateParams.LineItem.PriceData.Recurring.Interval.MONTH).build())
                                        .build())
                        .build());

        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }
}
