package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByService(Service service);
    List<Review> findByCustomer(User user);
}
