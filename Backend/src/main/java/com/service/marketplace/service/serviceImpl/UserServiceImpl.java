package com.service.marketplace.service.serviceImpl;

import com.service.marketplace.dto.request.SetProviderRequest;
import com.service.marketplace.dto.request.UserUpdateRequest;
import com.service.marketplace.dto.response.UserResponse;
import com.service.marketplace.exception.FileUploadException;
import com.service.marketplace.exception.RoleNotFoundException;
import com.service.marketplace.exception.UserNotFoundException;
import com.service.marketplace.mapper.UserMapper;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.CloudinaryService;
import com.service.marketplace.service.JwtService;
import com.service.marketplace.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CloudinaryService cloudinaryService;

    @Override
    public User getCurrentUser() { //YES
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UserNotFoundException("No authenticated user found");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getEmail();
            return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        } else {
            throw new UserNotFoundException("Authenticated user is not of expected type");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllByIsActive(true);
        return users.stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Integer userId) { //YES
        return userRepository.findById(userId)
                .map(userMapper::userToUserResponse)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserResponse updateUser(Integer userId, UserUpdateRequest userToUpdate, MultipartFile multipartFile) { //????????????
        User existingUser = null;

        if (userId != null) {
            existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        } else {
            existingUser = this.getCurrentUser();
            if (existingUser == null) {
                throw new UserNotFoundException("No authenticated user found");
            }
        }

        if (multipartFile != null) {
            String newPictureUrl = null;

            existingUser = this.getCurrentUser();


            if (multipartFile != null) {
                try {
                    newPictureUrl = cloudinaryService.uploadFile(multipartFile);
                } catch (IOException e) {
                    throw new FileUploadException("Error uploading picture", e);
                }
            }

            existingUser.setPicture(newPictureUrl);
        }

        String phoneNum = "";
        if (userToUpdate.getPhoneNumber() != null) {
            phoneNum = userToUpdate.getPhoneNumber();
        }
        existingUser.setFirstName(userToUpdate.getFirstName());
        existingUser.setLastName(userToUpdate.getLastName());
        existingUser.setPhoneNumber(phoneNum);
        existingUser.setDescription(userToUpdate.getDescription());

        userRepository.save(existingUser);

        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public UserResponse updateUserRole(Integer userId, SetProviderRequest providerRequest) { //YES
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String roleName = providerRequest.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found!"));

        if (!existingUser.getRoles().contains(role)) {
            existingUser.getRoles().add(role);
            User updatedUser = userRepository.save(existingUser);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public UserResponse updateUserRoleToProvider(Integer userId) { //???????????????????
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Role role = roleRepository.findByName("PROVIDER").orElseThrow(() -> new RoleNotFoundException("Role not found!"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            User updatedUser = userRepository.save(user);
            return userMapper.userToUserResponse(updatedUser);
        }

        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse getUserResponseByUser(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setRoles(user.getRoles());
        return userMapper.userToUserResponse(user);
    }

    @Override
    public boolean deleteUserById(Integer userId) {  //YES
        return userRepository.findById(userId).map(user -> {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
