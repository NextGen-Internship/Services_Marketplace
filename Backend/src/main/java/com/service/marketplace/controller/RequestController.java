package com.service.marketplace.controller;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.dto.response.RequestResponse;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/request")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RequestResponse>> getAllRequests() {
        List<RequestResponse> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<RequestResponse> getRequestById(@PathVariable("requestId") Integer requestId) {
        RequestResponse requestResponse = requestService.getRequestById(requestId);

        if (requestResponse != null) {
            return ResponseEntity.ok(requestResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<RequestResponse> createRequest(@RequestBody RequestRequest requestToCreate) {
        RequestResponse requestResponse = requestService.createRequest(requestToCreate);
        return ResponseEntity.ok(requestResponse);
    }


    @PutMapping("/{requestId}")
    public ResponseEntity<RequestResponse> updateRequest(@PathVariable("requestId") Integer requestId, @RequestBody RequestRequest requestToUpdate) {
        try {
            RequestResponse updatedRequestResponse = requestService.updateRequest(requestId, requestToUpdate);
            if (updatedRequestResponse == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedRequestResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("requestId") Integer requestId) {
        requestService.deleteRequestById(requestId);
        return ResponseEntity.ok().build();
    }

}