package com.service.marketplace.dto.request;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ServiceCreateRequest {
    @NotEmpty
    @Size(min = 2, max = 50)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 200)
    private String description;

    private ServiceStatus serviceStatus;

    @NotEmpty
    private BigDecimal price;

    @NotEmpty
    private Integer providerId;

    @NotEmpty
    private Integer categoryId;

    private List<Integer> cityIds;

    MultipartFile multipartFile;
}
