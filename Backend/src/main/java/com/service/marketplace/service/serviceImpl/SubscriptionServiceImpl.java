package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.SubscriptionRequest;
import com.service.marketplace.dto.response.SubscriptionResponse;
import com.service.marketplace.mapper.SubscriptionMapper;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.Subscription;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.SubscriptionRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;

    @Override
    public List<SubscriptionResponse> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        return subscriptionMapper.toSubscriptionResponseList(subscriptions);
    }

    @Override
    public SubscriptionResponse getSubscriptionById(Integer subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);

        return (subscription != null) ? subscriptionMapper.subscriptionToSubscriptionResponse(subscription) : null;
    }

//    @Override
//    public SubscriptionResponse updateSubscription(Integer subscriptionId, SubscriptionRequest subscriptionToUpdate) {
//        Subscription existingSubscription = subscriptionRepository.findById(subscriptionId).orElse(null);
//
//        Subscription updatedSubscription = subscriptionMapper.subscriptionRequestToSubscription(subscriptionToUpdate);
//
//        if (existingSubscription != null) {
//            existingSubscription.setStripeId(updatedSubscription.getStripeId());
//            existingSubscription.setEndDate(updatedSubscription.getEndDate());
//            existingSubscription.setStartDate(updatedSubscription.getStartDate());
//            existingSubscription.setActive(updatedSubscription.get);
//            existingSubscription.setStartDate(updatedSubscription.getStartDate());
//
//            return cityMapper.cityToCityResponse(cityRepository.save(existingCity));
//        } else {
//            return null;
//        }
//    }

    @Override
    public void deleteSubscriptionById(Integer subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public SubscriptionResponse getSubscriptionByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            List<Subscription> userSubscriptions = subscriptionRepository.findByUser(user);
            Optional<Subscription> latestSubscriptionOptional = userSubscriptions.stream()
                    .max(Comparator.comparing(Subscription::getStartDate));

            Subscription latestSubscription = latestSubscriptionOptional.orElse(null);

            return subscriptionMapper.subscriptionToSubscriptionResponse(latestSubscription);
        } else {
            return new SubscriptionResponse();
        }
    }
}
