package com.service.marketplace.dto.request;

import lombok.Data;

@Data
public class StripeRequest {

    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;
    private String username;
    private boolean success;

}
