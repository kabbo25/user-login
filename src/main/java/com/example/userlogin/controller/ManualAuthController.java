package com.example.userlogin.controller;

import com.example.userlogin.dto.AuthResponse;
import com.example.userlogin.dto.LoginRequest;
import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.service.ManualUserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manual")
@Profile("manual")
public class ManualAuthController {

    private final ManualUserService userService;
    private final AuthenticationManager authenticationManager;

    public ManualAuthController(ManualUserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest request) {
        boolean success = userService.registerUser(request);
        
        if (success) {
            return ResponseEntity.ok(new AuthResponse(true, "User registered successfully", request.getUsername()));
        } else {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Registration failed - user may already exist"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new AuthResponse(true, "Login successful via manual AuthenticationManager", request.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Invalid credentials: " + e.getMessage()));
        }
    }

    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("User profile for: " + auth.getName() + " - Approach: Manual DaoAuthenticationProvider & AuthenticationManager");
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Admin dashboard for: " + auth.getName() + " - Approach: Manual DaoAuthenticationProvider & AuthenticationManager");
    }
}