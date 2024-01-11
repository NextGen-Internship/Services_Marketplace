package com.service.marketplace.service;

import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.persistence.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer userId);
    User updateUser(Integer userId, UserUpdateRequest userToUpdate);
    void deleteUserById(Integer userId);
}
