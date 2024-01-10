package com.service.marketplace.service;

import com.service.marketplace.persistence.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllRequests();
    Request getRequestById(Integer requestId);
    Request createRequest(Request requestToCreate);
    Request updateRequest(Integer requestId, Request requestToUpdate);
    void deleteRequestById(Integer requestId);
}