package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceCreateRequest;
import com.service.marketplace.dto.request.ServiceFilterRequest;
import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();

    ServiceResponse getServiceById(Integer serviceId);

    ServiceResponse createService(ServiceRequest serviceToCreate, List<MultipartFile> files);

    ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate);

    void deleteServiceById(Integer serviceId);

    List<ServiceResponse> getServicesByUserId(Integer userId);

    Page<ServiceResponse> fetchServices(Integer page, Integer pageSize, String sortingField, String sortingDirection);

    Page<ServiceResponse> filterServices(ServiceFilterRequest serviceFilterRequest);

    List<ServiceResponse> getServicesByCurrentUser();

//    void uploadPicture(String url, Integer entityId);
}
