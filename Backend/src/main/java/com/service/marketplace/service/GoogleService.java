package com.service.marketplace.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.service.marketplace.dto.response.AuthenticationResponse;
import com.service.marketplace.persistence.entity.Role;
import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.repository.RoleRepository;
import com.service.marketplace.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class GoogleService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private RoleRepository roleRepository;


    public AuthenticationResponse verifyGoogleToken(String googleToken) throws IOException, GeneralSecurityException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .build();

        GoogleIdToken idToken = verifier.verify(googleToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();


            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            try {
                User existingUser = userRepository.findByEmail(payload.getEmail()).orElseThrow();
                String jwtToken = jwtService.generateToken(existingUser);
                return new AuthenticationResponse(jwtToken);

            } catch (Exception e) {
                Role role = roleRepository.findByName("CUSTOMER").orElseThrow();
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                User newUser = new User();
                newUser.setEmail(payload.getEmail());
                newUser.setFirstName(givenName);
                newUser.setLastName(familyName);
                newUser.setPicture(pictureUrl);
                newUser.setRoles(roles);
                userRepository.save(newUser);
                String jwtToken = jwtService.generateToken(newUser);
                return new AuthenticationResponse(jwtToken);
            }
        }
        return null;

    }
}



