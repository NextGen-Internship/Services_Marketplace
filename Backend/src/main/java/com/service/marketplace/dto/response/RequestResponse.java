package com.service.marketplace.dto.response;

import lombok.Data;

@Data
public class RequestResponse {
    private Integer customerId;
    private Integer serviceId;
    private String description;
    private boolean isActive;
}
