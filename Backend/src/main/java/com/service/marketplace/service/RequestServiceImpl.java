package com.service.marketplace.service;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.mapper.RequestMapper;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.repository.RequestRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Request getRequestById(Integer requestId) {
        return requestRepository.findById(requestId).orElse(null);
    }

    @Override
    public Request createRequest(RequestRequest requestToCreate) {
        Request newRequest = requestMapper.requestRequestToRequest(requestToCreate);
        return requestRepository.save(newRequest);
    }

    @Override
    public Request updateRequest(Integer requestId, RequestRequest requestToUpdate) {
        Request existingRequest = requestRepository.findById(requestId).orElse(null);

        if (existingRequest != null) {
            existingRequest = requestMapper.requestRequestToRequest(requestToUpdate);

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
