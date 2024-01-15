package com.service.marketplace.controller;

import com.service.marketplace.dto.request.RequestRequest;
import com.service.marketplace.persistence.entity.Request;
import com.service.marketplace.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/request")
public class RequestController {
    private final RequestService requestService;

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
    public ResponseEntity<Request> createRequest(@RequestBody RequestRequest requestToCreate) {
        Request newRequest = requestService.createRequest(requestToCreate);
        return ResponseEntity.ok(newRequest);
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<Request> updateRequest(@PathVariable("requestId") Integer requestId, @RequestBody RequestRequest requestToUpdate) {
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