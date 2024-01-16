package com.service.marketplace.mapper;

import java.util.List;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "cities", source = "cities")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Service serviceRequestToService(ServiceRequest request, User provider, Category category, List<City> cities);

    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "cities", source = "cities")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Service serviceRequestToService(ServiceRequest request, Category category, List<City> cities);

    @Mapping(target = "providerId", source = "provider")
    @Mapping(target = "categoryId", source = "category")
    @Mapping(target = "cityIds", source = "cities")
    ServiceResponse serviceToServiceResponse(Service service, Integer provider, Integer category, List<Integer> cities);

    List<ServiceResponse> toServiceResponseList(List<Service> services);
}
