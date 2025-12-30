package com.ingeduardo.demostore.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ingeduardo.demostore.dto.AuthResponse;
import com.ingeduardo.demostore.jwt.JwtService;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.User;
import com.ingeduardo.demostore.repository.RoleRepository;
import com.ingeduardo.demostore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Value("${google.client.id}")
    private String googleClientId;

    public GoogleAuthService(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse loginWithGoogle(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                // For Google users, we might not set a password or set a random one
                newUser.setPassword("");

                // Assign default role USER
                Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                    Role role = new Role();
                    role.setName("USER");
                    return roleRepository.save(role);
                });

                List<Role> roles = new ArrayList<>();
                roles.add(userRole);
                newUser.setRoles(roles);

                return userRepository.save(newUser);
            });

            String token = jwtService.generateToken(user);
            return new AuthResponse(token);
        } else {
            throw new RuntimeException("Invalid Google ID Token");
        }
    }
}
