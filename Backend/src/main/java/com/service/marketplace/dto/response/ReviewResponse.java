package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.enums.ServiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponse {
    private Integer id;
    private String description;
    private boolean isActive;
    private double rating;
    private Integer customerId;
    private Integer serviceId;
    // private List<Integer> filesIds;
    private LocalDateTime updatedAt;
}
