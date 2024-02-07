package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapping;

import java.util.List;

public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "reviewRequest.description")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Review reviewRequestToReview(ReviewRequest reviewRequest, User customer, Service service);

    @Mapping(target = "description", source = "reviewRequest.description")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Review reviewRequestToReview(ReviewRequest reviewRequest, Service service);

    ReviewResponse reviewToReviewResponse(Review review);

    List<ReviewResponse> toReviewResponseList(List<Review> reviews);
}
