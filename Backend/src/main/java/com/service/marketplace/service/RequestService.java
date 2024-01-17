package com.service.marketplace.service;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.persistence.entity.Request;

import java.util.List;

public interface RequestService {
    List<RequestResponse> getAllRequests();
    RequestResponse getRequestById(Integer requestId);
    RequestResponse createRequest(RequestRequest requestToCreate);
    RequestResponse updateRequest(Integer requestId, RequestRequest requestToUpdate);
    void deleteRequestById(Integer requestId);
}