package com.service.marketplace.service;

import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(Integer userId);

    UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate);

    boolean deleteUserById(Integer userId);

}
