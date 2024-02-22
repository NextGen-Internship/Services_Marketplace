package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.RequestToCreateDto;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.exception.RequestNotFoundException;
import com.service.marketplace.exception.ServiceNotFoundException;
import com.service.marketplace.exception.UserNotFoundException;
import com.service.marketplace.exception.VipServiceNotFoundException;
import com.service.marketplace.mapper.RequestMapper;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RequestRepository;
import com.service.marketplace.persistence.repository.ServiceRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.RequestService;
import com.service.marketplace.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final ServiceRepository serviceRepository;
    private final UserService userService;


    @Override
    public List<RequestResponse> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requestMapper.toRequestResponseList(requests);
    }

    @Override
    public RequestResponse getRequestById(Integer requestId) {
        Request requestEntity = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        //if (requestEntity != null) {
            return requestMapper.requestToRequestResponse(requestEntity);
//        }
//        return null;
    }

    @Override
    public RequestResponse createRequest(RequestToCreateDto requestToCreate) {
        User customer = userRepository.findById(requestToCreate.getCustomerId()).orElseThrow(() -> new UserNotFoundException());
        com.service.marketplace.persistence.entity.Service existingService = serviceRepository.findById(requestToCreate.getServiceId()).orElseThrow(() -> new ServiceNotFoundException());
        Request newRequest = requestMapper.requestRequestToRequest(requestToCreate,
                customer, existingService);
        Request savedRequest = requestRepository.save(newRequest);
        return requestMapper.requestToRequestResponse(savedRequest);
    }

    @Override
    public RequestResponse updateRequest(Integer requestId, RequestToCreateDto requestToUpdate) {
        Request existingRequest = requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        //if (existingRequest != null) {
            requestMapper.requestFromRequest(requestToUpdate, existingRequest);
            Request updatedRequest = requestRepository.save(existingRequest);
            return requestMapper.requestToRequestResponse(updatedRequest);
//        } else {
//            return null;
//        }
    }

    @Override
    public void deleteRequestById(Integer requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new RequestNotFoundException(requestId);
        }
        requestRepository.deleteById(requestId);
    }

    @Override
    public List<RequestResponse> getRequestsByProvider() {
        User providerUser = userService.getCurrentUser();
        List<com.service.marketplace.persistence.entity.Service> servicesList = serviceRepository.findByProvider(providerUser);
        List<Request> requests = new ArrayList<>();
        for (com.service.marketplace.persistence.entity.Service service : servicesList) {
            List<Request> serviceRequest = requestRepository.findByService(service);
            requests.addAll(serviceRequest);

        }
        return requestMapper.toRequestResponseList(requests);
    }
}
