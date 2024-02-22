package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.exception.VipServiceNotFoundException;
import com.service.marketplace.mapper.VipServiceMapper;
import com.service.marketplace.persistence.entity.VipService;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.persistence.repository.VipServiceRepository;
import com.service.marketplace.service.VipServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VipServiceImpl implements VipServiceService {
    //CORRECT ! ! !

    private final UserRepository userRepository;
    private final VipServiceRepository vipServiceRepository;
    private final VipServiceMapper vipServiceMapper;

    @Override
    public List<VipServiceResponse> getAllVipServices() {
        List<VipService> vipServices = vipServiceRepository.findAll();
        return vipServiceMapper.toVipServiceResponseList(vipServices);
    }

    @Override
    public VipServiceResponse getVipServiceById(Integer vipServiceId) { //YES
        VipService vipService = vipServiceRepository.findById(vipServiceId).orElseThrow(() -> new VipServiceNotFoundException(vipServiceId));

        return //(vipService != null) ?
                vipServiceMapper.toVipServiceResponse(vipService);
        //: null;
    }

    @Override
    public void deleteVipServiceById(Integer vipServiceId) {
        if (!vipServiceRepository.existsById(vipServiceId)) {
            throw new VipServiceNotFoundException(vipServiceId);
        }
        vipServiceRepository.deleteById(vipServiceId);
    }



}
