package com.service.marketplace.service.serviceImplementation;

import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceServiceImplementation implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImplementation(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.service.marketplace.persistence.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public com.service.marketplace.persistence.entity.Service getServiceById(Integer serviceId) {
        return serviceRepository.findById(serviceId).orElse(null);
    }

    @Override
    public List<com.service.marketplace.persistence.entity.Service> getAllServicesByCategory(Category category) {
        return serviceRepository.findByCategory(category);
    }

    @Override
    public com.service.marketplace.persistence.entity.Service createService(com.service.marketplace.persistence.entity.Service serviceToCreate) {
        serviceToCreate.setCreatedAt(LocalDateTime.now());
        serviceToCreate.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(serviceToCreate);
    }

    @Override
    public com.service.marketplace.persistence.entity.Service updateService(Integer serviceId, com.service.marketplace.persistence.entity.Service serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);

        if (existingService != null) {
            existingService.setTitle(serviceToUpdate.getTitle());
            existingService.setDescription(serviceToUpdate.getDescription());
            existingService.setPrice(serviceToUpdate.getPrice());

            existingService.setCreatedAt(LocalDateTime.now());
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
