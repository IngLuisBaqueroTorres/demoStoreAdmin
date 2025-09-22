package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.AuthResponse;
import com.ingeduardo.demostore.dto.LoginRequest;
import com.ingeduardo.demostore.dto.RegisterRequest;
import com.ingeduardo.demostore.jwt.JwtService;
import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.User;
import com.ingeduardo.demostore.repository.RoleRepository;
import com.ingeduardo.demostore.repository.UserRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.ingeduardo.demostore.model.enums.RoleName;

@Service
public class AuthService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationManager authenticationManager;

        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException("This email is already registered");
                }
                List<Role> roles = request.getRoles().stream()
                                .map(roleEnum -> {
                                        Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
                                        return optionalRole.orElseGet(() -> {
                                                Role newRole = new Role();
                                                newRole.setName(roleEnum);
                                                return roleRepository.save(newRole);
                                        });
                                })
                                .collect(Collectors.toList());

                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRoles(roles);

                userRepository.save(user);

                String token = jwtService.generateToken(user);
                return new AuthResponse(token);
        }

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtService.generateToken(user);
                return new AuthResponse(token);
        }
}
