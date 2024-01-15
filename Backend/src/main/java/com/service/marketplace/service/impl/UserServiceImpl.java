package com.service.marketplace.service.impl;

import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.mapper.UserMapper;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllByIsActive(true);
        return users.stream()
                .map(UserMapper.INSTANCE::userToUserResponse)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? UserMapper.INSTANCE.userToUserResponse(user) : null;
    }


    @Override
    public UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserMapper.INSTANCE.updateUserFromRequest(userToUpdate, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.INSTANCE.userToUserResponse(updatedUser);
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
