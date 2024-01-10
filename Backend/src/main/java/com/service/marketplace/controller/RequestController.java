package com.service.marketplace.controller;

import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/requests")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequestById(@PathVariable("requestId") Integer requestId) {
        Request request = requestService.getRequestById(requestId);

        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestBody Request requestToCreate) {
        Request newRequest = requestService.createRequest(requestToCreate);
        return ResponseEntity.ok(newRequest);
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<Request> updatedRequest(@PathVariable("requestId") Integer requestId, @RequestBody Request requestToUpdate) {
        try {
            Request updatedRequest = requestService.updateRequest(requestId, requestToUpdate);

            if (updatedRequest == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("requestId") Integer requestId) {
        requestService.deleteRequestById(requestId);
        return ResponseEntity.ok().build();
    }
}
