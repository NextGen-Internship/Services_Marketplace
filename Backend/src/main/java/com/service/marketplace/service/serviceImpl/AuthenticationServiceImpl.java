package com.service.marketplace.service.serviceImpl;


import com.service.marketplace.dto.request.AuthenticationRequest;
import com.service.marketplace.dto.request.RegisterRequest;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.exception.RoleNotFoundException;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.AuthenticationService;
import com.service.marketplace.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new RoleNotFoundException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .isActive(true)
                .build();
        userRepository.save(user);
        return null;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}