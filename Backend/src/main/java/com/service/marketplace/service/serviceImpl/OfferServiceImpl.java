package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.OfferRequest;
import com.service.marketplace.dto.response.OfferResponse;
import com.service.marketplace.exception.OfferNotFoundException;
import com.service.marketplace.exception.RequestNotFoundException;
import com.service.marketplace.exception.UserNotFoundException;
import com.service.marketplace.mapper.OfferMapper;
import com.service.marketplace.persistence.entity.Offer;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.OfferRepository;
import com.service.marketplace.persistence.repository.RequestRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.OfferService;
import com.service.marketplace.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;
    //CORRECT ! ! !

    @Override
    public List<OfferResponse> getAllOffer() {
        List<Offer> offers = offerRepository.findAll();
        return offerMapper.toOfferResponseList(offers);
    }

    @Override
    public OfferResponse getOfferById(Integer offerId) {
        Offer offerEntity = offerRepository.findById(offerId).orElseThrow(() -> new OfferNotFoundException(offerId));
        //if (offerEntity != null) {
            return offerMapper.offerToOfferResponse(offerEntity);
//        }
//        return null;
    }

    @Override
    public OfferResponse createOffer(OfferRequest offerToCreate) {
        User provider = userRepository.findById(offerToCreate.getProvider_id()).orElseThrow(() -> new UserNotFoundException("User not found!"));
        Request existingRequest = requestRepository.findById(offerToCreate.getRequest_id()).orElseThrow(() -> new RequestNotFoundException());
        Offer newOffer = offerMapper.OfferRequestToOffer(offerToCreate, provider, existingRequest);
        Offer savedOffer = offerRepository.save(newOffer);
        return offerMapper.offerToOfferResponse(savedOffer);
    }

    @Override
    public OfferResponse updateOffer(Integer offerId, OfferRequest offerToUpdate) {
        Offer existingOffer = offerRepository.findById(offerId).orElse(null);
        if (existingOffer != null) {
            Offer updateOffer = offerMapper.OfferRequestToOffer(offerToUpdate);
            existingOffer.setOfferStatus(updateOffer.getOfferStatus());
            offerRepository.save(existingOffer);
            return offerMapper.offerToOfferResponse(existingOffer);
        } else {
            return null;
        }
    }


    @Override
    public void deleteOfferById(Integer offerId) {
        if (!offerRepository.existsById(offerId)) {
            throw new OfferNotFoundException(offerId);
        }
        offerRepository.deleteById(offerId);
    }

    @Override
    public List<OfferResponse> getOfferByCustomer() {
        User customerUser = userService.getCurrentUser();
        List<Request> requestList = requestRepository.findByCustomer(customerUser);
        List<Offer> offers = new ArrayList<>();
        for (Request request : requestList) {
            List<Offer> requestOffer = offerRepository.findByRequest(request);
            offers.addAll(requestOffer);
        }
        return offerMapper.toOfferResponseList(offers);
    }

    public ResponseEntity<String> cancelOffer(OfferRequest offerRequest, Integer offerId) {
        try {
            Offer existingOffer = offerRepository.findById(offerId).orElse(null);
            existingOffer.setOfferStatus(offerRequest.getOfferStatus());
            offerRepository.save(existingOffer);
        } catch (Exception e) {
            System.err.println("Offer update error: " + e.getMessage());
        }
        return null;
    }
}

