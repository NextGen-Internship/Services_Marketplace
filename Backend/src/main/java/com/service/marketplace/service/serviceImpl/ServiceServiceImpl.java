package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.mapper.ServiceMapper;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.CategoryRepository;
import com.service.marketplace.persistence.repository.CityRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Override
    public List<ServiceResponse> getAllServices() {
        List<com.service.marketplace.persistence.entity.Service> services = serviceRepository.findAll();

        return serviceMapper.toServiceResponseList(services);
    }

    @Override
    public ServiceResponse getServiceById(Integer serviceId) {
        com.service.marketplace.persistence.entity.Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service != null) {
            List<Integer> cityIds = new ArrayList<>();
            for (City city : service.getCities()) {
                cityIds.add(city.getId());
            }

            return serviceMapper.serviceToServiceResponse(service);
        } else {
            return null;
        }
    }

    @Override
    public ServiceResponse createService(ServiceRequest serviceToCreate) {
        List<City> cities = cityRepository.findAllById(serviceToCreate.getCityIds());
        User provider = userRepository.findById(serviceToCreate.getProviderId()).orElse(null);
        Category category = categoryRepository.findById(serviceToCreate.getCategoryId()).orElse(null);

        com.service.marketplace.persistence.entity.Service newService = serviceMapper.serviceRequestToService(serviceToCreate,
                provider, category, cities);


        return serviceMapper.serviceToServiceResponse(serviceRepository.save(newService));
    }

    @Override
    public ServiceResponse updateService(Integer serviceId, ServiceRequest serviceToUpdate) {
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(serviceId).orElse(null);
        Category category = categoryRepository.findById(serviceToUpdate.getCategoryId()).orElse(null);
        List<City> cities = cityRepository.findAllById(serviceToUpdate.getCityIds());

        com.service.marketplace.persistence.entity.Service updatedService = serviceMapper.serviceRequestToService(serviceToUpdate, category, cities);

        if (existingService != null) {
            existingService.setTitle(updatedService.getTitle());
            existingService.setDescription(updatedService.getDescription());
            existingService.setServiceStatus(updatedService.getServiceStatus());
            existingService.setPrice(updatedService.getPrice());
            existingService.setCities(updatedService.getCities());

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
    public Page<ServiceResponse> fetchServices(Integer page, Integer pageSize, String sortingField, String sortingDirection) {
        Sort sort = Sort.by(Sort.Direction.valueOf(sortingDirection), sortingField);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return serviceMapper.toServiceResponsePage(serviceRepository.findAll(pageable));
    }

    @Override
    public Page<ServiceResponse> filterServices(BigDecimal minPrice, BigDecimal maxPrice, List<Integer> categoryIds,
                                                List<Integer> providerIds, List<Integer> cityIds, Integer page, Integer pageSize,
                                                String sortingField, String sortingDirection) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(sortingDirection), sortingField));
        Page<com.service.marketplace.persistence.entity.Service> filteredServices = serviceRepository.filterServices(
                minPrice, maxPrice, categoryIds, providerIds, cityIds, pageable);

        return serviceMapper.toServiceResponsePage(filteredServices);
    }


}
