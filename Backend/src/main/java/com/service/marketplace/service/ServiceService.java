package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;

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
}
