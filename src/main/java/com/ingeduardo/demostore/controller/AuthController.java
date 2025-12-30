package com.ingeduardo.demostore.controller;

import com.ingeduardo.demostore.dto.AuthResponse;
import com.ingeduardo.demostore.dto.LoginRequest;
import com.ingeduardo.demostore.dto.RegisterRequest;
import com.ingeduardo.demostore.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private com.ingeduardo.demostore.service.GoogleAuthService googleAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @RequestBody com.ingeduardo.demostore.dto.GoogleLoginRequest request)
            throws java.security.GeneralSecurityException, java.io.IOException {
        AuthResponse response = googleAuthService.loginWithGoogle(request.getToken());
        return ResponseEntity.ok(response);
    }
}
