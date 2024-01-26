package com.service.marketplace.controller;

import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class StorageController {
    private final StorageService service;
    private final UserRepository userRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userEmail = ((UserResponse) authentication.getPrincipal()).getEmail();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        return new ResponseEntity<>(service.uploadFile(file, user.getId()), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }

    @GetMapping("/getPicture")
    public ResponseEntity<String> getPicture() throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = ((User) authentication.getPrincipal()).getId();
        return new ResponseEntity<>(service.getPicture(userId).toString(), HttpStatus.OK);
    }
}
