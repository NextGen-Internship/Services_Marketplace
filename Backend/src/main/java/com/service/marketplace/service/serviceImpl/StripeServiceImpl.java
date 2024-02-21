package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.exception.*;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.SubscriptionRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.persistence.repository.VipServiceRepository;
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

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class StripeServiceImpl implements StripeService {
    private static final Gson gson = new Gson();
    private final UserService userService;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final VipServiceRepository vipServiceRepository;
    private final SubscriptionRepository subscriptionRepository;

    private String vipSessionId = "";
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

            User user = userRepository.findByEmail(stripeAccountRequest.getEmail()).orElseThrow(() -> new UserNotFoundException());

            user.setStripeAccountId(account.getId());
            userRepository.save(user);

            return account.getId();
        } catch (StripeException e) {
            throw new StripeAccountCreationException("Failed to create Stripe account.", e);
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
            throw new StripeSessionCreationException();
        }
    }


    @Override
    public String getProductPrice(String priceId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Price price = Price.retrieve(priceId);

            return gson.toJson(price);
        } catch (StripeException e) {
            throw new StripeServiceException("Failed to retrieve product price: " + e.getMessage(), "STRIPE_SERVICE_EXCEPTION");
        }
    }

    @Override
    public ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader) {
        String endpointSecret = "whsec_cc0df325d4cf2f830514ec91c6e23dde3a856062b86ec456d8aa4791581aa91d";

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new StripeServiceException("Failed signature verification", "SIGNATURE_VERIFICATION_FAILED");
        } catch (Exception e) {
            throw new StripeServiceException("Error handling webhook event: " + e.getMessage(), "WEBHOOK_HANDLING_ERROR");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
        }

        String userEmail = extractUserEmailFromPayload(payload);

        switch (event.getType()) {
            case "checkout.session.completed": {
                try {
                    String sessionId = null;

                    System.out.println("!!! Checking session id...");
                    Session session = (Session) stripeObject;
                    sessionId = session.getId();

                    System.out.println("!!! Session id: " + sessionId);

                    String priceId = "price_1OhuIFI2KDxgMJyoGon8NKEY";

                    try {
                        Customer customer = Customer.list(CustomerListParams.builder().setEmail(userEmail).build()).getData().get(0);
                        String customerId = customer.getId();

                        Map<String, String> metadata = getMetadataFromPaymentIntent(sessionId);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(metadata);
                        if (!metadata.isEmpty() && metadata.containsKey("serviceType") && "VIP".equals(metadata.get("serviceType"))) {
                            System.out.println("===== VIP service detected");
                            PaymentIntentListParams paymentIntentParams = PaymentIntentListParams.builder()
                                    .setCustomer(customerId)
                                    .build();
                            PaymentIntentCollection paymentIntents = PaymentIntent.list(paymentIntentParams);

                            PaymentIntent latestPaymentIntent = paymentIntents.getData().stream()
                                    .max(Comparator.comparing(PaymentIntent::getCreated))
                                    .orElse(null);
                            System.out.println(latestPaymentIntent);

                            String serviceIdString = metadata.get("serviceId");
                            Integer serviceId = Integer.parseInt(serviceIdString);

                            Optional<com.service.marketplace.persistence.entity.Service> serviceOptional = serviceRepository.findById(serviceId);

                            Optional.ofNullable(latestPaymentIntent)
                                    .filter(paymentIntent -> serviceOptional.isPresent())
                                    .ifPresent(s -> updateVipService(serviceOptional.get(), s));

                            return ResponseEntity.status(HttpStatus.OK).body("VIP service created");
                        } else {

                            List<Subscription> subscriptions;
                            try {
                                subscriptions = Subscription.list(SubscriptionListParams.builder().setCustomer(customerId).build()).getData();
                            } catch (StripeException e) {
                                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
                            }

                            boolean hasActiveSubscription = subscriptions.stream().anyMatch(subscription -> "active".equals(subscription.getStatus()));
                            User userToBeUpdated = userRepository.findByEmail(userEmail).orElse(null);
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
                        }
                    } catch (StripeException e) {
                        throw new StripeServiceException("Error handling webhook event: " + e.getMessage(), "STRIPE_EXCEPTION");
                    }


                } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving customer data from Stripe");
                }

                break;
            }
            case "payment_intent.created": {
                System.out.println("Webhook for created payment intent");

                break;
            }
            case "customer.subscription.created": {
                System.out.println("Webhook for created subscription");
                break;
            }
            case "customer.subscription.deleted": {
                break;
            }
            case "customer.subscription.updated": {
                System.out.println("Webhook for updated subscription");
                break;
            }
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok().build();
    }

    private void updateVipService(com.service.marketplace.persistence.entity.Service service, PaymentIntent latestPaymentIntent) {
        com.service.marketplace.persistence.entity.VipService vipService = new com.service.marketplace.persistence.entity.VipService();
        vipService.setStripeId(latestPaymentIntent.getId());
        vipService.setStartDate(new Date(latestPaymentIntent.getCreated() * 1000L));
        vipService.setEndDate(new Date((latestPaymentIntent.getCreated() + 14L * 24 * 60 * 60) * 1000L));
        vipService.setActive(true);
        vipService.setService(service);

        service.setVip(true);
        serviceRepository.save(service);
        vipServiceRepository.save(vipService);
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
                    //throw new StripeServiceException("The subscription cancellation was unsuccessful " + e.getM)
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The subscription is not active and it cannot be canceled.");
            }
        } catch (StripeException e) {
            throw new StripeServiceException(("Error with stripe interaction: " + e.getMessage()), "STRIPE_EXCEPTION");
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with stripe interaction: " + e.getMessage());
        }
    }

    private String extractUserEmailFromPayload(String payload) {
        try {
            JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

            if (jsonObject.has("data") && !jsonObject.get("data").isJsonNull()) {
                JsonObject data = jsonObject.getAsJsonObject("data").getAsJsonObject("object").getAsJsonObject("customer_details");
                if (data.has("email")) {
                    String email = data.get("email").getAsString();
                    return email;
                } else {
                    throw new PayloadProcessingException("Missing email field in payload");
                }
            } else {
                throw new PayloadProcessingException("Missing or null 'data' field in payload");
            }
        } catch (Exception e) {
            throw new PayloadProcessingException("Error extracting user email from payload", e);
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
                    User user = userRepository.findById(subscription.getUser().getId()).orElseThrow(() -> new UserNotFoundException());

                    subscription.setActive(false);
                    subscriptionRepository.save(subscription);

                    Set<Role> userRoles = user.getRoles();
                    Role role = new Role("PROVIDER");
                    userRoles.remove(role);

                    user.setRoles(userRoles);

                    userRepository.save(user);
                }
            } catch (StripeException e) {
               throw new StripeServiceException("Error: " + e.getMessage(), "STRIPE_EXCEPTION");
            }
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkVipServiceStatus() {
        Stripe.apiKey = stripeApiKey;

        List<com.service.marketplace.persistence.entity.VipService> vipServices = vipServiceRepository.findAll();

        for (com.service.marketplace.persistence.entity.VipService vipService : vipServices) {
            try {
                PaymentIntent stripeVipService = PaymentIntent.retrieve(vipService.getStripeId());

                com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(vipService.getService().getId()).orElse(null);
                service.setVip(false);
                serviceRepository.save(service);

                vipService.setActive(false);
                vipServiceRepository.save(vipService);

            } catch (StripeException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public String vipWithCheckoutPage(Checkout checkout) {
        Stripe.apiKey = stripeApiKey;

        SessionCreateParams params = new SessionCreateParams.Builder()
                .setSuccessUrl(checkout.getSuccessUrl())
                .setCancelUrl(checkout.getCancelUrl())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L)
                        .setPrice(checkout.getPriceId())
                        .build())
                .putMetadata("serviceType", "VIP")
                .putMetadata("serviceId", checkout.getServiceId())
                .setCustomerEmail(checkout.getEmail())
                .build();


        try {
            Session session = Session.create(params);
//            session.setMetadata(new HashMap<String, String>() {{
//                put("sessionId", session.getId());
//            }});
            //vipSessionId = session.getId();
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

    public Map<String, String> getMetadataFromPaymentIntent(String sessionId) {
        Stripe.apiKey = stripeApiKey;
        String priceId = "price_1OhuIFI2KDxgMJyoGon8NKEY";

        try {
            // Retrieve the session
            Session session = Session.retrieve(sessionId);
            Price price = Price.retrieve(priceId);
            String productId = price.getProduct();
            Product product = Product.retrieve(productId);

            // Get the PaymentIntent ID from the session
            String paymentIntentId = session.getPaymentIntent();

            if (paymentIntentId == null) {
                // Handle the case where there is no PaymentIntent associated with the session
                log.error("No PaymentIntent associated with the session: {}", sessionId);
                return Collections.emptyMap();
            }

            // Retrieve the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Access the metadata
            Map<String, String> metadata = session.getMetadata();

            return metadata;
        } catch (StripeException e) {
            log.error("Failed to retrieve PaymentIntent or Session: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

}