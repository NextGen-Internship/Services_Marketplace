package com.service.marketplace.controller;

import com.service.marketplace.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloudinary")
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PutMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam(value = "entityId", required = false) Integer entityId,
                                             @RequestParam(value = "entityType", required = false, defaultValue = "USER") String entityType)
            throws IOException {
        return new ResponseEntity<>(cloudinaryService.uploadFile(multipartFile, entityId, entityType), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile() {
        return new ResponseEntity<>(cloudinaryService.deleteFile(), HttpStatus.OK);
    }

    @GetMapping("/getPicture")
    public ResponseEntity<String> getPicture() throws MalformedURLException {
        return new ResponseEntity<>(cloudinaryService.getPicture(), HttpStatus.OK);
    }
}
