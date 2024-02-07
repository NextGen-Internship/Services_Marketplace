package com.service.marketplace.service;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.persistence.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User getCurrentUser();

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Integer userId);

    UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate);

    boolean deleteUserById(Integer userId);

    UserResponse updateUserRole(Integer userId, SetProviderRequest providerRequest);

    UserResponse getUserResponseByUser(User user);

    void uploadPicture(String url);
}
