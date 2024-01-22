package com.service.marketplace.controller;

import com.service.marketplace.dto.request.StripeChargeRequest;
import com.service.marketplace.dto.request.StripeRequest;
import com.service.marketplace.service.StripeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@AllArgsConstructor
public class StripeController {


    private final StripeService stripeService;


    @PostMapping("/card/token")
    @ResponseBody
    public StripeRequest createCardToken(@RequestBody StripeRequest model) {


        return stripeService.createCardToken(model);
    }

    @PostMapping("/charge")
    @ResponseBody
    public StripeChargeRequest charge(@RequestBody StripeChargeRequest model) {

        return stripeService.charge(model);
    }



}
