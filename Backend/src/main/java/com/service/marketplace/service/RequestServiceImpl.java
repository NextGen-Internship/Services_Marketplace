package com.service.marketplace.service;

import com.service.marketplace.dto.request.RequestToCreateDto;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.mapper.RequestMapper;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RequestRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ServiceRepository serviceRepository;


    @Override
    public List<RequestResponse> getAllRequests() {
        List<com.service.marketplace.persistence.entity.Request> requests = requestRepository.findAll();
        return requestMapper.toRequestResponseList(requests);
    }

    @Override
    public RequestResponse getRequestById(Integer requestId) {
        com.service.marketplace.persistence.entity.Request requestEntity = requestRepository.findById(requestId).orElse(null);
        if (requestEntity != null) {
            return requestMapper.requestToRequestResponse(requestEntity);
        }
        return null;
    }

    @Override
    public RequestResponse createRequest(RequestToCreateDto requestToCreate) {
        User customer = userRepository.findById(requestToCreate.getCustomerId()).orElse(null);
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(requestToCreate.getServiceId()).orElse(null);
        com.service.marketplace.persistence.entity.Request newRequest = requestMapper.requestRequestToRequest(requestToCreate,
                customer, existingService);
        Request savedRequest = requestRepository.save(newRequest);
        return requestMapper.requestToRequestResponse(savedRequest);
    }


    @Override
    public RequestResponse updateRequest(Integer requestId, RequestToCreateDto requestToUpdate) {
        Request existingRequest = requestRepository.findById(requestId).orElse(null);
        if (existingRequest != null) {
            requestMapper.requestFromRequest(requestToUpdate, existingRequest);
            Request updatedRequest = requestRepository.save(existingRequest);
            return requestMapper.requestToRequestResponse(updatedRequest);
        } else {
            return null;
        }
    }


    @Override
    public void deleteRequestById(Integer requestId) {
        requestRepository.deleteById(requestId);
    }
}
