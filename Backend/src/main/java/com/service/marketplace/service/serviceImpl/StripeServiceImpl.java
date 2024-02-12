package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.SubscriptionRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.StripeService;
import com.service.marketplace.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class StripeServiceImpl implements StripeService {
    private static final Gson gson = new Gson();
    private final UserService userService;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    @Value("${STRIPE_PRIVATE_KEY}")
    private String stripeApiKey;

    @Override
    public String createStripeAccount(StripeAccountRequest stripeAccountRequest) {
        String[] data = stripeAccountRequest.getDateOfBirth().split("-");

        try {
            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Stripe.apiKey = stripeApiKey;
            long currentUnixTimestamp = (int) (System.currentTimeMillis() / 1000);

            Map<String, Object> externalAccountParams = new HashMap<>();
            externalAccountParams.put("object", "bank_account");
            externalAccountParams.put("country", "BG");
            externalAccountParams.put("currency", "BGN");
            externalAccountParams.put("account_holder_name", "Account Holder");
            externalAccountParams.put("account_holder_type", "individual");
            externalAccountParams.put("account_number", stripeAccountRequest.getIban());
            externalAccountParams.put("default_for_currency", true);

                Map<String, Object> tokenParams = new HashMap<>();
                tokenParams.put("bank_account", externalAccountParams);
                Token token = Token.create(tokenParams);
                String bankAccountToken = token.getId();

            AccountCreateParams accountCreateParams = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.CUSTOM)
                    .setCountry("BG")
                    .setEmail(stripeAccountRequest.getEmail())
                    .setCapabilities(AccountCreateParams.Capabilities.builder()
                            .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                            .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                            .build())
                    .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                    .setTosAcceptance(AccountCreateParams.TosAcceptance.builder()
                            .setDate(currentUnixTimestamp)
                            .setIp(ClientIp.getClientIp(httpRequest))
                            .build())
                    .setBusinessProfile(AccountCreateParams.BusinessProfile.builder()
                            .setMcc("7277")
                            .setProductDescription("Service")
                            .build())
                    .setIndividual(AccountCreateParams.Individual.builder()
                            .setFirstName(stripeAccountRequest.getFirstMiddleName())
                            .setLastName(stripeAccountRequest.getLastName())
                            .setEmail(stripeAccountRequest.getEmail())
                            .setDob(AccountCreateParams.Individual.Dob.builder()
                                    .setDay(Long.parseLong(data[2]))
                                    .setMonth(Long.parseLong(data[1]))
                                    .setYear(Long.parseLong(data[0]))
                                    .build())
                            .setAddress(AccountCreateParams.Individual.Address.builder()
                                    .setLine1(stripeAccountRequest.getAddress())
                                    .setCity(stripeAccountRequest.getCity())
                                    .setState("BG")
                                    .setPostalCode(stripeAccountRequest.getPostalCode())
                                    .setCountry("BG")
                                    .build())
                            .setPhone(stripeAccountRequest.getPhoneNumber())
                            .build())
                    .setExternalAccount(bankAccountToken)
                    .build();

            Account account = Account.create(accountCreateParams);
            account.setPayoutsEnabled(true);

            User user = userRepository.findByEmail(stripeAccountRequest.getEmail()).orElse(null);

            if (user != null) {
                user.setStripeAccountId(account.getId());
            } else {
                throw new RuntimeException("Failed to create Stripe account.");
            }
            userRepository.save(user);

            return account.getId();
        } catch (StripeException e) {
            log.error("Failed to create Stripe account: {}", e.getMessage());
            throw new RuntimeException("Failed to create Stripe account.", e);
        }
    }

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

    @Override
    public ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader) {
        String endpointSecret = "whsec_cc0df325d4cf2f830514ec91c6e23dde3a856062b86ec456d8aa4791581aa91d";

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // You may want to handle this case appropriately.
        }

        String userEmail = extractUserEmailFromPayload(payload);

        switch (event.getType()) {
            case "checkout.session.completed": {
                try {
                    Customer customer = Customer.list(CustomerListParams.builder().setEmail(userEmail).build()).getData().get(0);
                    String customerId = customer.getId();

                    // Retrieve all subscriptions associated with the customer's email address from Stripe
                    List<Subscription> subscriptions;
                    try {
                        subscriptions = Subscription.list(SubscriptionListParams.builder().setCustomer(customerId).build()).getData();
                    } catch (StripeException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
                    }

                    // Check if any subscription is active
                    boolean hasActiveSubscription = subscriptions.stream().anyMatch(subscription -> "active".equals(subscription.getStatus()));
                    User userToBeUpdated = userRepository.findByEmail(userEmail).orElse(null);
                    // If there's an active subscription, change the user's role to 'Provider'
                    if (hasActiveSubscription) {
                        if (userToBeUpdated != null) {
                            userService.updateUserRoleToProvider(userToBeUpdated.getId());
                        }
                    }

                    long currentPeriodStart = subscriptions.get(0).getCurrentPeriodStart();
                    long currentPeriodEnd = subscriptions.get(0).getCurrentPeriodEnd();

                    java.util.Date startDate = new java.util.Date(currentPeriodStart * 1000);
                    java.util.Date endDate = new java.util.Date(currentPeriodEnd * 1000);

                    String subscriptionStatus = subscriptions.get(0).getStatus();
                    boolean isActive = "active".equals(subscriptionStatus);

                    com.service.marketplace.persistence.entity.Subscription subscription = new com.service.marketplace.persistence.entity.Subscription();
                    subscription.setStripeId(subscriptions.get(0).getId());
                    subscription.setStartDate(startDate);
                    subscription.setEndDate(endDate);
                    subscription.setUser(userToBeUpdated);
                    subscription.setActive(isActive);

                    subscriptionRepository.save(subscription);
                } catch (StripeException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving customer data from Stripe");
                }

                break;
            }
            case "customer.subscription.created": {
                System.out.println("Webhook for created subscription");
                break;
            }
            case "customer.subscription.deleted": {
                // Handle customer.subscription.deleted event
                break;
            }
            case "customer.subscription.updated": {
                System.out.println("Webhook for updated subscription");
                break;
            }
            // Add cases to handle other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> cancelSubscription(String stripeId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Subscription subscription = Subscription.retrieve(stripeId);

            if ("active".equals(subscription.getStatus())) {
                SubscriptionUpdateParams params =
                        SubscriptionUpdateParams.builder().setCancelAtPeriodEnd(true).build();

                Subscription canceledSubscription = subscription.update(params);

                if (canceledSubscription.getCancelAtPeriodEnd()) {
                    return ResponseEntity.ok("Subscription is successfully canceled.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The subscription cancellation was unsuccessful.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subscription is not active and it cannot be canceled.");
            }
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with stripe interaction: " + e.getMessage());
        }
    }

    private String extractUserEmailFromPayload(String payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

            if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
                // Extract the email field from the JSON object
                JsonObject data = jsonObject.getAsJsonObject("data").getAsJsonObject("object").getAsJsonObject("customer_details");
                if (data.has("email")) {
                    String email = data.get("email").getAsString();
                    return email;
                } else {
                    System.err.println("Missing email field in payload");
                    return null;
                }
            } else {
                System.err.println("Missing or null 'data' field in payload");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error extracting user email from payload: " + e.getMessage());
            return null;
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkSubscriptionsStatus() {
        Stripe.apiKey = stripeApiKey;

        List<com.service.marketplace.persistence.entity.Subscription> subscriptions = subscriptionRepository.findAll();

        for (com.service.marketplace.persistence.entity.Subscription subscription : subscriptions) {
            try {
                Subscription stripeSubscription = Subscription.retrieve(subscription.getStripeId());

                if ("canceled".equals(stripeSubscription.getStatus())) {
                    User user = userRepository.findById(subscription.getUser().getId()).orElse(null);

                    subscription.setActive(false);
                    subscriptionRepository.save(subscription);

                    Set<Role> userRoles = user.getRoles();
                    Role role = new Role("PROVIDER");
                    userRoles.remove(role);

                    user.setRoles(userRoles);

                    userRepository.save(user);
                }
            } catch (StripeException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

}