package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.response.SubscriptionResponse;
import com.service.marketplace.exception.SubscriptionNotFoundException;
import com.service.marketplace.exception.UserNotFoundException;
import com.service.marketplace.mapper.SubscriptionMapper;
import com.service.marketplace.persistence.entity.Subscription;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.SubscriptionRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public SubscriptionResponse getSubscriptionById(Integer subscriptionId) { //YES
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        //return (subscription != null) ? subscriptionMapper.subscriptionToSubscriptionResponse(subscription) : null;
        return subscriptionMapper.subscriptionToSubscriptionResponse(subscription);
    }
    @Override
    public void deleteSubscriptionById(Integer subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    public SubscriptionResponse getSubscriptionByUserId(Integer userId) {  //YES
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

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
