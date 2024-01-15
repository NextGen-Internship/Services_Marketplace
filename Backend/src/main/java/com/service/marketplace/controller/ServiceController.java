package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/services")
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping("/all")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        List<ServiceResponse> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable("serviceId") Integer serviceId) {
        ServiceResponse service = serviceService.getServiceById(serviceId);

        if (service != null) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest serviceToCreate) {
        ServiceResponse newService = serviceService.createService(serviceToCreate);
        return ResponseEntity.ok(newService);
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable("serviceId") Integer serviceId,
                                                         @RequestBody ServiceRequest serviceToUpdate) {
        ServiceResponse updatedService = serviceService.updateService(serviceId, serviceToUpdate);

        if (updatedService != null) {
            return ResponseEntity.ok(updatedService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable("serviceId") Integer serviceId) {
        serviceService.deleteServiceById(serviceId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<List<ServiceResponse>> getServicesByCategory(@PathVariable("categoryId") Integer categoryId) {
//        List<ServiceResponse> services = serviceService.getAllServicesByCategory(categoryId);
//
//        if (services.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(services);
//        }
//    }
//
//    @GetMapping("/provider/{providerId}")
//    public ResponseEntity<List<ServiceResponse>> getServicesByProvider(@PathVariable("providerId") Integer providerId) {
//        List<ServiceResponse> services = serviceService.getAllServicesByProvider(providerId);
//
//        if (services.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(services);
//        }
//    }

    @GetMapping("/filter")
    public ResponseEntity<List<ServiceResponse>> getFilteredServices(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer providerId) {

        List<ServiceResponse> filteredServices;

        if (categoryId != null) {
            filteredServices = serviceService.getAllServicesByCategory(categoryId);
        } else if (providerId != null) {
            filteredServices = serviceService.getAllServicesByProvider(providerId);
        } else {
            filteredServices = serviceService.getAllServices();
        }

        return filteredServices.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(filteredServices);
    }

}
