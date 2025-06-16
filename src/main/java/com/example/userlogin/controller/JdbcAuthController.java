package com.example.userlogin.controller;

import com.example.userlogin.dto.AuthResponse;
import com.example.userlogin.dto.LoginRequest;
import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.service.JdbcUserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jdbc")
@Profile("jdbc")
public class JdbcAuthController {

    private final JdbcUserService userService;

    public JdbcAuthController(JdbcUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest request) {
        boolean success = userService.registerUser(request);
        
        if (success) {
            return ResponseEntity.ok(new AuthResponse(true, "User registered successfully", request.getUsername()));
        } else {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Registration failed - user or email may already exist"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        boolean authenticated = userService.authenticateUser(request.getUsername(), request.getPassword());
        
        if (authenticated) {
            return ResponseEntity.ok(new AuthResponse(true, "Login successful", request.getUsername()));
        } else {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Invalid credentials"));
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("User profile for: " + auth.getName() + " - Approach: JdbcUserDetailsManager");
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Admin dashboard for: " + auth.getName() + " - Approach: JdbcUserDetailsManager");
    }
}