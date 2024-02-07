package com.service.marketplace.service;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.request.ServiceFilterRequest;
import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getAllReviews();

    ReviewResponse getReviewById(Integer reviewId);

    ReviewResponse createReview(ReviewRequest reviewToCreate);

    ReviewResponse updateReview(Integer reviewId, ReviewRequest reviewToUpdate);

    void deleteReviewById(Integer reviewId);

    List<ReviewResponse> getReviewsByCustomerId(Integer customerId);

    List<ReviewResponse> getReviewsByServiceId(Integer serviceId);

    void uploadPicture(String url, Integer entityId);
}
