package com.service.marketplace.controller;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/services")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<Service> getServiceById(@PathVariable("serviceId") Integer serviceId) {
        Service service = serviceService.getServiceById(serviceId);

        if (service != null) {
            return ResponseEntity.ok(service);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{categoryId}")
//    public ResponseEntity<List<Service>> getServicesByCategory(@PathVariable("categoryId") Category category) {
//        List<Service> services = serviceService.getAllServicesByCategory(category);
//
//        if (services.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(services);
//        }
//    }
//
//    @GetMapping("/{providerId}")
//    public ResponseEntity<List<Service>> getServicesByProvider(@PathVariable("providerId") User user) {
//        List<Service> services = serviceService.getAllServicesByProvider(user);
//
//        if (services.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(services);
//        }
//    }

    @PostMapping("/create")
    public ResponseEntity<Service> createService(@RequestBody ServiceRequest serviceToCreate) {
        Service newService = serviceService.createService(serviceToCreate);
        return ResponseEntity.ok(newService);
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<Service> updateService(@PathVariable("serviceId") Integer serviceId, @RequestBody ServiceRequest serviceToUpdate) {
        try {
            Service updatedService = serviceService.updateService(serviceId, serviceToUpdate);

            if (updatedService == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedService);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable("serviceId") Integer serviceId) {
        serviceService.deleteServiceById(serviceId);
        return ResponseEntity.ok().build();
    }
}
