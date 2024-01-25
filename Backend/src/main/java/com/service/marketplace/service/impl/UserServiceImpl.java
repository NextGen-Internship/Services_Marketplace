package com.service.marketplace.service.impl;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.mapper.UserMapper;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.enums.UserRole;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllByIsActive(true);
        return users.stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(userMapper::userToUserResponse)
                .orElse(null);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userToUserResponse)
                .orElse(null);
    }

    @Override
    public UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate) {
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            userMapper.updateUserFromRequest(userToUpdate, existingUser);
            User updatedUser = userRepository.save(existingUser);
            return userMapper.userToUserResponse(updatedUser);
        } else {
            return null;
        }
    }

    @Override
    public UserResponse updateUser(String userEmail, UserUpdateRequest userToUpdate) {
        User existingUser = userRepository.findByEmail(userEmail).orElse(null);

        return updateUser(existingUser.getId(), userToUpdate);
    }

    @Override
    public UserResponse updateUserRole(Integer userId, SetProviderRequest providerRequest) {
        User existingUser = userRepository.findById(userId).orElse(null);

        String roleName = providerRequest.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        if (!existingUser.getRoles().contains(role)) {
            existingUser.getRoles().add(role);
            User updatedUser = userRepository.save(existingUser);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(existingUser);
    }


    @Override
    public boolean deleteUserById(Integer userId) {
        return userRepository.findById(userId).map(user -> {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

}
