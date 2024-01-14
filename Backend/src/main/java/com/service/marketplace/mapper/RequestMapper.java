package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.persistence.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    Request requestRequestToRequest(RequestRequest request);

    RequestResponse requestToRequestResponse(Request request);

    void requestFromRequest(RequestRequest requestRequest, @MappingTarget Request request);
}