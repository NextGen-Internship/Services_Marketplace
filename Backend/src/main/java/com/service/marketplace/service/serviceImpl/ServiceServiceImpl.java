package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.mapper.ServiceMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
        Category category = categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null);
        User provider = userRepository.findById(serviceToCreate.getProviderId()).orElse(null);

        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate, provider, category);

        return serviceRepository.save(newService);
    }

    @Override
    public com.service.marketplace.persistence.entity.Service updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);

        Category category = categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null);
        User provider = userRepository.findById(serviceToUpdate.getProviderId()).orElse(null);

        if (provider == null) {
            return null;
        }

        if (existingService != null) {
            existingService = serviceMapper.serviceRequestToService(serviceToUpdate, category);

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
