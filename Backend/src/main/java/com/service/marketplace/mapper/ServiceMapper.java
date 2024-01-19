package com.service.marketplace.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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

    @Mapping(target = "providerId", source = "service.provider.id")
    @Mapping(target = "categoryId", source = "service.category.id")
    @Mapping(target = "cityIds", source = "service.cities", qualifiedByName = "cityListToIntegerList")
    ServiceResponse serviceToServiceResponse(Service service);

    List<ServiceResponse> toServiceResponseList(List<Service> services);

    @Named("cityListToIntegerList")
    static List<Integer> cityListToIntegerList(List<City> cities) {
        return cities.stream()
                .map(City::getId)
                .collect(Collectors.toList());
    }
}
