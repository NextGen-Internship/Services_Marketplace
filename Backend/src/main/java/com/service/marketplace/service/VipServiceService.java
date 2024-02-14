package com.service.marketplace.service;

import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.persistence.entity.VipService;
import com.service.marketplace.persistence.repository.VipServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface VipServiceService {
    List<VipServiceResponse> getAllVipServices();
    VipServiceResponse getVipServiceById(Integer vipServiceId);
    void deleteVipServiceById(Integer vipServiceId);


}

