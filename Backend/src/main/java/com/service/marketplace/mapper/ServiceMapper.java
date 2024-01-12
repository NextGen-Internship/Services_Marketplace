package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.ServiceRequest;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.persistence.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    Service serviceRequestToService(ServiceRequest request);

    ServiceResponse serviceToServiceResponse(Service service);

    void serviceFromRequest(ServiceRequest request, @MappingTarget Service service);
}
