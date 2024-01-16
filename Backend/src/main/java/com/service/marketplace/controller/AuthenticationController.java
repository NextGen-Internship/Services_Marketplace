package com.service.marketplace.controller;


import com.service.marketplace.dto.request.AuthenticationRequest;
import com.service.marketplace.dto.request.RegisterRequest;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.persistence.repository.UserRepository;
import com.service.marketplace.service.AuthenticationService;
import com.service.marketplace.service.JwtService;
import com.service.marketplace.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;
    private final GoogleService googleService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse authenticationResponse = service.login(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/google/login")
    public ResponseEntity<AuthenticationResponse> googleLogin(
            @RequestBody AuthenticationResponse authenticationResponse
    ) throws GeneralSecurityException, IOException {

        System.out.println(authenticationResponse.getToken());
        AuthenticationResponse userResponse = googleService.verifyGoogleToken(authenticationResponse.getToken());
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
