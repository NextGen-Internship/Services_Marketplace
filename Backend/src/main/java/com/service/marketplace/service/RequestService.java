package com.service.marketplace.service;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.persistence.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllRequests();
    Request getRequestById(Integer requestId);
    Request createRequest(RequestRequest requestToCreate);
    Request updateRequest(Integer requestId, RequestRequest requestToUpdate);
    void deleteRequestById(Integer requestId);
}