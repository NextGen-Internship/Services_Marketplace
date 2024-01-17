package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.persistence.entity.*;
import com.service.marketplace.persistence.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "description")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Request requestRequestToRequest(RequestRequest request, User customer, Service service);
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "serviceId", source = "service.id")
    RequestResponse requestToRequestResponse(Request request);

    void requestFromRequest(RequestRequest requestRequest, @MappingTarget Request request);





}