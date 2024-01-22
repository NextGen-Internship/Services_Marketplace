package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<ServiceResponse> updateService(@PathVariable("serviceId") Integer serviceId, @RequestBody ServiceRequest serviceToUpdate) {
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

    @GetMapping("/filter")
    public Page<ServiceResponse> getFilteredServices(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Integer> categoryIds,
            @RequestParam(required = false) List<Integer> providerIds,
            @RequestParam(required = false) List<Integer> cityIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "updatedAt") String sortingField,
            @RequestParam(defaultValue = "asc") String sortingDirection) {

        return serviceService.filterServices(minPrice, maxPrice, categoryIds, providerIds, cityIds, page, pageSize, sortingField, sortingDirection);
    }

    @GetMapping("/getPaginationServices/{page}/{pageSize}/{sortingField}/{sortingDirection}")
    public Page<ServiceResponse> getServices(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize,
                                             @PathVariable("sortingField") String sortingField, @PathVariable("sortingDirection") String sortingDirection) {
        return serviceService.fetchServices(page, pageSize, sortingField, sortingDirection);
    }
}
