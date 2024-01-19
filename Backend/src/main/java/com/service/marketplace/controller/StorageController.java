package com.service.marketplace.controller;

import com.service.marketplace.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class StorageController {
    private final StorageService service;


    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadFile(@PathVariable int userId, @RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file,userId), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
