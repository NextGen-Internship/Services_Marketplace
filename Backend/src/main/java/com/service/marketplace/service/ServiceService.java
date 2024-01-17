package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
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

    List<ServiceResponse> sortBasedUponSomeField(String field);

    Page<ServiceResponse> getServiceWithPagination(Integer offset, Integer pageSize);
    Page<ServiceResponse> getServiceWithPaginationAndSorting(Integer offset, Integer pageSize, String field);
}
