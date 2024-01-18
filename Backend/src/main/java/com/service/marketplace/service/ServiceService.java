package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();

    ServiceResponse getServiceById(Integer serviceId);

    ServiceResponse createService(ServiceRequest serviceToCreate);

    ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate);

    void deleteServiceById(Integer serviceId);

    List<ServiceResponse> getAllServicesByCategory(Integer categoryId);

    List<ServiceResponse> getAllServicesByProvider(Integer providerId);

    List<ServiceResponse> getAllServicesByCity(Integer cityId);

    Page<ServiceResponse> fetchServices(Integer page, Integer pageSize, String sortingField, String sortingDirection);
}
