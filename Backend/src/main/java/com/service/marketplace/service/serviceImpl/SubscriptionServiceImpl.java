package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.service.SubscriptionService;
import com.service.marketplace.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    public String getProductPrice(String priceId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Price price = Price.retrieve(priceId);

            return gson.toJson(price);
        } catch (StripeException e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
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
