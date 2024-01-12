package com.service.marketplace.mapper;

import java.time.LocalDateTime;
import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Service serviceRequestToService(ServiceRequest request, User provider, Category category);

    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Service serviceRequestToService(ServiceRequest request, Category category);

    ServiceResponse serviceToServiceResponse(Service service);

    void serviceFromRequest(ServiceRequest request, @MappingTarget Service service);
}
