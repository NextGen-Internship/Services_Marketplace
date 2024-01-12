package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.mapper.ServiceMapper;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public List<com.service.marketplace.persistence.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public com.service.marketplace.persistence.entity.Service getServiceById(Integer serviceId) {
        return serviceRepository.findById(serviceId).orElse(null);
    }

//    @Override
//    public List<com.service.marketplace.persistence.entity.Service> getAllServicesByCategory(Category category) {
//        return serviceRepository.findByCategory(category);
//    }
//
//    @Override
//    public List<com.service.marketplace.persistence.entity.Service> getAllServicesByProvider(User user) {
//        return serviceRepository.findByProvider(user);
//    }

    @Override
    public com.service.marketplace.persistence.entity.Service createService(ServiceRequest serviceToCreate) {
        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate);
        newService.setCreatedAt(LocalDateTime.now());
        newService.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(newService);
    }

    @Override
    public com.service.marketplace.persistence.entity.Service updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);

        if (existingService != null) {
            com.service.marketplace.persistence.entity.Service updatedService = serviceMapper.serviceRequestToService(serviceToUpdate);
            existingService.setTitle(updatedService.getTitle());
            existingService.setDescription(updatedService.getDescription());
            existingService.setPrice(updatedService.getPrice());

            existingService.setUpdatedAt(LocalDateTime.now());

            return serviceRepository.save(existingService);
        } else {
            return null;
        }
    }

    @Override
    public void deleteServiceById(Integer serviceId) {
        serviceRepository.deleteById(serviceId);
    }
}
