package com.service.marketplace.service;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;

import java.util.List;

public interface ServiceService {
    List<Service> getAllServices();
    Service getServiceById(Integer serviceId);
//    List<Service> getAllServicesByCategory(Category category);
//    List<Service> getAllServicesByProvider(User user);
    Service createService(ServiceRequest serviceToCreate);
    Service updateService(Integer serviceId, ServiceRequest serviceToUpdate);
    void deleteServiceById(Integer serviceId);
}
