package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.mapper.ReviewMapper;
import com.service.marketplace.persistence.entity.*;
import com.service.marketplace.persistence.repository.ReviewRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviewMapper.toReviewResponseList(reviews);
    }

    @Override
    public ReviewResponse getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (review != null) {
            return reviewMapper.reviewToReviewResponse(review);
        } else {
            return null;
        }
    }

    @Override
    public ReviewResponse createReview(ReviewRequest reviewToCreate) {
        // List<City> cities = cityRepository.findAllById(serviceToCreate.getCityIds());
        User customer = userRepository.findById(reviewToCreate.getCustomerId()).orElse(null);
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(reviewToCreate.getServiceId()).orElse(null);

        Review newReview = reviewMapper.reviewRequestToReview(reviewToCreate, customer, service/*, cities*/);

        return reviewMapper.reviewToReviewResponse(reviewRepository.save(newReview));
    }

    @Override
    public ReviewResponse updateReview(Integer reviewId, ReviewRequest reviewToUpdate) {
        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(reviewToUpdate.getServiceId()).orElse(null);
        // List<City> cities = cityRepository.findAllById(serviceToUpdate.getCityIds());

        Review updatedReview = reviewMapper.reviewRequestToReview(reviewToUpdate, service/*, cities*/);

        if (existingReview != null) {
            existingReview.setDescription(updatedReview.getDescription());
            existingReview.setActive(updatedReview.get());
            existingReview.setRating(updatedReview.getRating());
            // existingReview.setCities(updatedReview.getCities());

            return reviewMapper.reviewToReviewResponse(reviewRepository.save(existingReview));
        } else {
            return null;
        }
    }

    @Override
    public void deleteReviewById(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewResponse> getReviewsByCustomerId(Integer customerId) {
        User user = userRepository.findById(customerId).orElse(null);

        if (user != null) {
            List<Review> userReviews = reviewRepository.findByCustomer(user);

            return reviewMapper.toReviewResponseList(userReviews);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReviewResponse> getReviewsByServiceId(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service != null) {
            List<Review> serviceReviews = reviewRepository.findByService(service);

            return reviewMapper.toReviewResponseList(serviceReviews);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void uploadPicture(String url, Integer entityId) {

    }
}
