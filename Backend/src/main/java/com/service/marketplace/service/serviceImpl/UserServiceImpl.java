package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.mapper.UserMapper;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.JwtService;
import com.service.marketplace.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Data
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getEmail();
            return userRepository.findByEmail(email).orElse(null);
        }

        return null;
    }

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
    public UserResponse updateUserRoleToProvider(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        Role role = roleRepository.findByName("PROVIDER").orElseThrow(() -> new IllegalArgumentException("Role not found."));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            User updatedUser = userRepository.save(user);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse getUserResponseByUser(User user) {
        return userMapper.userToUserResponse(user);
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
