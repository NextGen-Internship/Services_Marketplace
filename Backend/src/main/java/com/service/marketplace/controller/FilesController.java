package com.service.marketplace.controller;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.FilesResponse;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.dto.response.ServiceResponse;
import com.service.marketplace.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FilesController {
    private final FilesService filesService;

    @GetMapping("/all")
    public ResponseEntity<List<FilesResponse>> getAllFiles() {
        List<FilesResponse> files = filesService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FilesResponse> getFileById(@PathVariable("fileId") Integer fileId) {
        FilesResponse file = filesService.getFileById(fileId);

        if (file != null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<FilesResponse>> getFilesByServiceId(@PathVariable("serviceId") Integer serviceId) {
        List<FilesResponse> serviceFiles = filesService.getFilesByServiceId(serviceId);
        return ResponseEntity.ok(serviceFiles);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<FilesResponse>> getFilesByReviewId(@PathVariable("reviewId") Integer reviewId) {
        List<FilesResponse> reviewFiles = filesService.getFilesByReviewId(reviewId);
        return ResponseEntity.ok(reviewFiles);
    }


    @PostMapping("/create")
    public ResponseEntity<FilesResponse> createFile(@RequestBody FilesRequest fileToCreate, @RequestParam(value = "file") MultipartFile multipartFile) {
        FilesResponse newFile = filesService.createFile(fileToCreate, multipartFile);
        return ResponseEntity.ok(newFile);
    }

    @PutMapping("/update/{fileId}")
    public ResponseEntity<FilesResponse> updateFile(@PathVariable("fileId") Integer fileId, @RequestBody FilesRequest fileToUpdate, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        FilesResponse updatedFile = filesService.updateFile(fileId, fileToUpdate, multipartFile);

        if (updatedFile != null) {
            return ResponseEntity.ok(updatedFile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileId") Integer fileId) {
        filesService.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
