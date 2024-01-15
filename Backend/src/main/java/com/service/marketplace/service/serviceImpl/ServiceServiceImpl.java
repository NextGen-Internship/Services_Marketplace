package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.mapper.ServiceMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
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

        return serviceMapper.toServiceResponseList(services);
    }

    @Override
    public ServiceResponse getServiceById(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);
        return (service != null) ? serviceMapper.serviceToServiceResponse(service) : null;
    }

    @Override
    public ServiceResponse createService(ServiceRequest serviceToCreate) {
        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate,
                userRepository.findById(serviceToCreate.getProviderId()).orElse(null),
                categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null));


        return serviceMapper.serviceToServiceResponse(serviceRepository.save(newService));
    }

    @Override
    public ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);

        com.service.marketplace.persistence.entity.Service updatedService = serviceMapper.serviceRequestToService(serviceToUpdate,
                categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null));

        if (existingService != null) {
            existingService.setTitle(updatedService.getTitle());
            existingService.setDescription(updatedService.getDescription());
            existingService.setServiceStatus(updatedService.getServiceStatus());
            existingService.setPrice(updatedService.getPrice());

            return serviceMapper.serviceToServiceResponse(serviceRepository.save(existingService));
        } else {
            return null;
        }
    }

    @Override
    public void deleteServiceById(Integer serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    @Override
    public List<ServiceResponse> getAllServicesByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        List<com.service.marketplace.persistence.entity.Service> servicesOfCategory = serviceRepository.findByCategory(category);

        return serviceMapper.toServiceResponseList(servicesOfCategory);
    }

    @Override
    public List<ServiceResponse> getAllServicesByProvider(Integer providerId) {
        User provider = userRepository.findById(providerId).orElse(null);
        List<com.service.marketplace.persistence.entity.Service> servicesOfProvider = serviceRepository.findByProvider(provider);

        return serviceMapper.toServiceResponseList(servicesOfProvider);
    }

//    @Override
//    public List<ServiceResponse> getAllServicesByCity(String cityName) {
//        List<com.service.marketplace.persistence.entity.Service> servicesOfCity = serviceRepository.findByCity(cityName);
//        return servicesOfCity.stream()
//                .map(serviceMapper::serviceToServiceResponse)
//                .collect(Collectors.toList());
//    }
}
