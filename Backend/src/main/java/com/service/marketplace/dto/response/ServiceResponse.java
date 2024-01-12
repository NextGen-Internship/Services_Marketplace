package com.service.marketplace.dto.response;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceResponse {
    private  Integer id;
    private String title;
    private String description;
    private ServiceStatus serviceStatus;
    private BigDecimal price;
    private Integer providerId;
    private Integer categoryId;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}
