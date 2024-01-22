package com.service.marketplace.controller;

import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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