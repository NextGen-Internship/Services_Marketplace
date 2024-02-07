package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;

import java.util.List;

public interface ReviewMapper {
    Review reviewRequestToReview(ReviewRequest reviewRequest);
    ReviewResponse reviewToReviewResponse(Review review);
    List<ReviewResponse> toReviewResponseList(List<Review> reviews);
}
