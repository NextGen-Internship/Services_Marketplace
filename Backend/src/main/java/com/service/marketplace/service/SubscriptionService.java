package com.service.marketplace.service;

import com.service.marketplace.dto.request.CategoryRequest;
import com.service.marketplace.dto.request.SubscriptionRequest;
import com.service.marketplace.dto.response.CategoryResponse;
import com.service.marketplace.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionResponse> getAllSubscriptions();

    SubscriptionResponse getSubscriptionById(Integer subscriptionId);

    // SubscriptionResponse updateSubscription(Integer subscriptionId, SubscriptionRequest subscriptionToUpdate);

    void deleteSubscriptionById(Integer subscriptionId);

    SubscriptionResponse getSubscriptionByUserId(Integer userId);
}
