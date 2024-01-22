package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();

    ServiceResponse getServiceById(Integer serviceId);

    ServiceResponse createService(ServiceRequest serviceToCreate);

    ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate);

    void deleteServiceById(Integer serviceId);

    Page<ServiceResponse> fetchServices(Integer page, Integer pageSize, String sortingField, String sortingDirection);

    Page<ServiceResponse> filterServices(BigDecimal minPrice, BigDecimal maxPrice, List<Integer> categoryIds, List<Integer> providerIds, List<Integer> cityIds, Integer page, Integer pageSize, String sortingField, String sortingDirection);
}
