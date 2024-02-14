package com.service.marketplace.mapper;

import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.persistence.entity.VipService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VipServiceMapper {
    VipServiceResponse toVipServiceResponse(VipService vipService);
}
