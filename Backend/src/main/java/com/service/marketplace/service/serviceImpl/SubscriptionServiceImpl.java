package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.service.SubscriptionService;
import com.service.marketplace.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Gson gson = new Gson();
    private final UserService userService;
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
                .setCustomerEmail(checkout.getEmail())
                .setClientReferenceId(checkout.getUserId())
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

    @Override
    public Product getProductWithPrice(String productId, String priceId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        try {
            // Retrieve the product using the provided product ID
            Product product = Product.retrieve(productId);

            // Retrieve the price using the provided price ID
            Price price = Price.retrieve(priceId);

            // Attach the price information to the product
            product.setPrice(price);

            return product;
        } catch (StripeException e) {
            // Handle Stripe API exceptions
            throw new StripeException("Error retrieving product information: " + e.getMessage());
        }
    }

//    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request, String payload) {
//        String endpointSecret = "your_stripe_endpoint_secret"; // Replace with your actual Stripe endpoint secret
//
//        try {
//            // Verify the webhook signature
//            Event event = Webhook.constructEvent(
//                    payload,
//                    request.getHeader("Stripe-Signature"),
//                    endpointSecret
//            );
//
//            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
//            if (dataObjectDeserializer.getObject().isPresent()) {
//                Object object = dataObjectDeserializer.getObject().get();
//                if (object instanceof Session) {
//                    Session session = (Session) object;
//                    String clientReferenceId = session.getClientReferenceId();
//                    // Update user role to 'provider' based on clientReferenceId (assuming it holds user identifier)
//                    userService.updateUserRoleToProvider(Integer.parseInt(clientReferenceId));
//                }
//            }
//
//            return ResponseEntity.ok().build();
//        } catch (SignatureVerificationException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe signature");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
//        }
//    }
}
