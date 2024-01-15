package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ServiceResponse> getAllServices() {
        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findAll();
        return services.stream()
                .map(serviceMapper::serviceToServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponse getServiceById(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);
        return (service != null) ? serviceMapper.serviceToServiceResponse(service) : null;
    }

    @Override
    public ServiceResponse createService(ServiceRequest serviceToCreate) {
        // Map DTO to entity
        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate,
                userRepository.findById(serviceToCreate.getProviderId()).orElse(null),
                categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null));

        // Save entity and map it back to DTO for response
        return serviceMapper.serviceToServiceResponse(serviceRepository.save(newService));
    }

    @Override
    public ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        // Retrieve existing service
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);

        // Map DTO to entity
        com.service.marketplace.persistence.entity.Service updatedService = serviceMapper.serviceRequestToService(serviceToUpdate,
                categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null));

        if (existingService != null) {
            // Update existing service with new values
            existingService.setTitle(updatedService.getTitle());
            existingService.setDescription(updatedService.getDescription());
            existingService.setPrice(updatedService.getPrice());
            // ... other fields

            // Save updated entity and map it back to DTO for response
            return serviceMapper.serviceToServiceResponse(serviceRepository.save(existingService));
        } else {
            return null;
        }
    }

    @Override
    public void deleteServiceById(Integer serviceId) {
        serviceRepository.deleteById(serviceId);
    }

//    @Override
//    public List<com.service.marketplace.persistence.entity.Service> getAllServices() {
//        return serviceRepository.findAll();
//    }
//
//    @Override
//    public com.service.marketplace.persistence.entity.Service getServiceById(Integer serviceId) {
//        return serviceRepository.findById(serviceId).orElse(null);
//    }

//    @Override
//    public List<com.service.marketplace.persistence.entity.Service> getAllServicesByCategory(Category category) {
//        return serviceRepository.findByCategory(category);
//    }
//
//    @Override
//    public List<com.service.marketplace.persistence.entity.Service> getAllServicesByProvider(User user) {
//        return serviceRepository.findByProvider(user);
//    }

//    @Override
//    public com.service.marketplace.persistence.entity.Service createService(ServiceRequest serviceToCreate) {
//        Category category = categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null);
//        User provider = userRepository.findById(serviceToCreate.getProviderId()).orElse(null);
//
//        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate, provider, category);
//
//        return serviceRepository.save(newService);
//    }
//
//    @Override
//    public com.service.marketplace.persistence.entity.Service updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
//        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);
//
//        Category category = categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null);
//        User provider = userRepository.findById(serviceToUpdate.getProviderId()).orElse(null);
//
//        if (provider == null) {
//            return null;
//        }
//
//        if (existingService != null) {
//            existingService = serviceMapper.serviceRequestToService(serviceToUpdate, category);
//
//            return serviceRepository.save(existingService);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public void deleteServiceById(Integer serviceId) {
//        serviceRepository.deleteById(serviceId);
//    }
}
