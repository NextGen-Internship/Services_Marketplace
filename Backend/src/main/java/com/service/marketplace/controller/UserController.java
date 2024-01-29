package com.service.marketplace.controller;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.service.StorageService;
import com.service.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    private final StorageService storageService;

    @Autowired
    public UserController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Integer userId) {
        UserResponse userResponse = userService.getUserById(userId);

        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User user = userService.getCurrentUser();

        if (user != null) {
            return ResponseEntity.ok(userService.getUserById(user.getId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Valid
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("userId") Integer userId, @RequestBody UserUpdateRequest userToUpdate) {
        try {
            UserResponse updatedUser = userService.updateUser(userId, userToUpdate);

            if (updatedUser == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

//    @Valid
//    @PutMapping()
//    public ResponseEntity<UserResponse> updateCurrentUser(@ModelAttribute UserUpdateRequest userToUpdate,
//                                                          @RequestParam(value = "picture") Optional<MultipartFile> file) {
//        try {
//            User user = userService.getCurrentUser();
//            storageService.uploadFile(file.isPresent() ? file.get() : null);
//
//
//            UserResponse updatedUser = userService.updateUser(user.getId(), userToUpdate);
//
//            if (updatedUser == null) {
//                return ResponseEntity.notFound().build();
//            } else {
//                return ResponseEntity.ok(updatedUser);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }

    @Valid
    @PutMapping("/role/{userId}")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable("userId") Integer userId,
                                                       @RequestBody SetProviderRequest providerRequest) {
        try {
            UserResponse updatedUser = userService.updateUserRole(userId, providerRequest);
            if (updatedUser == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updatedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.ok("User soft deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}