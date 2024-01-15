package com.service.marketplace.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestRequest {

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Service ID cannot be null")
    private Long serviceId;

    private boolean isActive;
}
