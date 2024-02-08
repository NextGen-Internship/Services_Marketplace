package com.service.marketplace.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionResponse {
    private Integer userId;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String stripeId;
}
