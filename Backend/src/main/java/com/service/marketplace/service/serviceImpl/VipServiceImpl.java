package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.response.VipServiceResponse;
import com.service.marketplace.mapper.VipServiceMapper;
import com.service.marketplace.persistence.entity.VipService;
import com.service.marketplace.persistence.repository.VipServiceRepository;
import com.service.marketplace.service.VipServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VipServiceImpl implements VipServiceService {

    private final VipServiceRepository vipServiceRepository;
    private final VipServiceMapper vipServiceMapper;

    @Override
    public List<VipServiceResponse> getAllVipServices() {
        List<VipService> vipServices = vipServiceRepository.findByIsActiveTrue(); // Updated to fetch only active VIP services
        return vipServices.stream()
                .map(vipServiceMapper::toVipServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VipServiceResponse getVipServiceById(Integer id) {
        Optional<VipService> vipService = vipServiceRepository.findById(id);
        return vipService.map(vipServiceMapper::toVipServiceResponse).orElse(null);
    }

    @Override
    public void deleteVipServiceById(Integer id) {
        vipServiceRepository.findById(id).ifPresent(vipService -> {
            vipService.setActive(false);
            vipServiceRepository.save(vipService);
        });
    }


}
