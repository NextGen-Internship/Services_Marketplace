package com.service.marketplace.service;

import com.service.marketplace.persistence.entity.Service;

import java.util.List;

public interface ServiceService {
    List<Service> getAllServices();
    Service getServiceById(Integer serviceId);
    Service createService(Service serviceToCreate);
    Service updateService(Integer serviceId, Service serviceToUpdate);
    void deleteServiceById(Integer serviceId);
}
