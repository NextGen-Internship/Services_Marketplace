package com.service.marketplace.dto.request;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReviewRequest {
    @NotEmpty
    @Size(min = 10, max = 255)
    private String description;

    @NotEmpty
    @Size(min = 2, max = 200)
    private String description;

    private ServiceStatus serviceStatus;

    @NotEmpty
    private double price;

    @NotEmpty
    private Integer customerId;

    @NotEmpty
    private Integer serviceId;

    private List<Integer> filesIds;
}
