package com.service.marketplace.service;

import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImplementation implements RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImplementation(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Request getRequestById(Integer requestId) {
        return requestRepository.findById(requestId).orElse(null);
    }

    @Override
    public Request createRequest(Request requestToCreate) {
        return requestRepository.save(requestToCreate);
    }

    @Override
    public Request updateRequest(Integer requestId, Request requestToUpdate) {
        Request existingRequest = requestRepository.findById(requestId).orElse(null);

        if (existingRequest != null) {
            existingRequest.setDescription(requestToUpdate.getDescription());
            existingRequest.setCustomer(requestToUpdate.getCustomer());
            existingRequest.setCreatedAt(requestToUpdate.getCreatedAt());
            existingRequest.setUpdatedAt(requestToUpdate.getUpdatedAt());
            existingRequest.setActive(requestToUpdate.isActive());
            existingRequest.setService(requestToUpdate.getService());

            return requestRepository.save(existingRequest);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRequestById(Integer requestId) {
        requestRepository.deleteById(requestId);
    }
}
