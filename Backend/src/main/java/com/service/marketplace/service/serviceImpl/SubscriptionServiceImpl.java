package com.service.marketplace.service.serviceImpl;

import com.google.gson.Gson;
import com.service.marketplace.dto.request.Checkout;
import com.service.marketplace.dto.request.StripeAccountRequest;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.SubscriptionService;
import com.service.marketplace.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.TokenCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Gson gson = new Gson();
    private final UserService userService;
    private final UserRepository userRepository;
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
            externalAccountParams.put("country", "BG"); // Държава
            externalAccountParams.put("currency", "BGN"); // Валута
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

//    public boolean checkIfUserHasStripeAccount() throws StripeException {
//        User user = userService.getCurrentUser();
//        try {
//            CustomerListParams params = CustomerListParams.builder()
//                    .setEmail(user.getEmail())
//                    .build();
//
//            Iterable<Customer> customers = Customer.list(params).autoPagingIterable();
//
//            return customers.iterator().hasNext();
//        } catch (StripeException e) {
//            return false;
//        }
//    }

    @Override
    public String subscriptionWithCheckoutPage(Checkout checkout) {
        try {
            User user = userRepository.findByEmail(checkout.getEmail()).orElse(null);
            if (user == null) {
                return gson.toJson(Map.of("error", "User not found"));
            }

            Stripe.apiKey = stripeApiKey;

            // Retrieve customer object from Stripe using user's Stripe account ID
            Customer customer = Customer.retrieve(user.getStripeAccountId());
            Account account = Account.retrieve(user.getStripeAccountId());

            // Create session parameters
            SessionCreateParams params = new SessionCreateParams.Builder()
                    .setSuccessUrl(checkout.getSuccessUrl())
                    .setCancelUrl(checkout.getCancelUrl())
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomer(customer.getId())
                    .setCustomerEmail(checkout.getEmail())
                    .addLineItem(new SessionCreateParams.LineItem.Builder()
                            .setQuantity(1L)
                            .setPrice(checkout.getPriceId())
                            .build())
                    .build();

            // Create session and return session ID
            Session session = Session.create(params);
            return gson.toJson(Map.of("sessionId", session.getId()));
        } catch (StripeException e) {
            // Handle Stripe exceptions
            e.printStackTrace();
            return gson.toJson(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return gson.toJson(Map.of("error", "An unexpected error occurred"));
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

    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request, String payload) {
        String endpointSecret = "whsec_cc0df325d4cf2f830514ec91c6e23dde3a856062b86ec456d8aa4791581aa91d";

        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(
                    payload,
                    request.getHeader("Stripe-Signature"),
                    endpointSecret
            );

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            if (dataObjectDeserializer.getObject().isPresent()) {
                Object object = dataObjectDeserializer.getObject().get();
                if (object instanceof Session) {
                    Session session = (Session) object;
                    String clientReferenceId = session.getClientReferenceId();
                    userService.updateUserRoleToProvider(Integer.parseInt(clientReferenceId));
                }
            }

            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling webhook event");
        }
    }

}