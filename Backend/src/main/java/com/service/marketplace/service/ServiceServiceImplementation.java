package com.service.marketplace.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImplementation implements ServiceService {

    @Override
    public List<com.service.marketplace.persistence.entity.Service> getAllServices() {
        return null;
    }

    @Override
    public com.service.marketplace.persistence.entity.Service getServiceById(Integer serviceId) {
        return null;
    }

    @Override
    public com.service.marketplace.persistence.entity.Service createService(com.service.marketplace.persistence.entity.Service serviceToCreate) {
        return null;
    }

    @Override
    public com.service.marketplace.persistence.entity.Service updateService(Integer serviceId, com.service.marketplace.persistence.entity.Service serviceToUpdate) {
        return null;
    }

    @Override
    public void deleteServiceById(Integer serviceId) {

    }
}
