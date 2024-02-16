package com.service.marketplace.service;

import com.service.marketplace.dto.request.OfferRequest;
import com.service.marketplace.dto.response.OfferResponse;

import java.util.List;

public interface OfferService {

    List<OfferResponse> getAllOffer();

    OfferResponse getOfferById(Integer offerId);

    OfferResponse createOffer(OfferRequest offerToCreate);

    void deleteOfferById(Integer offerId);

    List<OfferResponse> getOfferByCustomer();
}
