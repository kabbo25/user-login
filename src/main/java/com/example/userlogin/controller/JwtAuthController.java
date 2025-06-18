package com.example.userlogin.controller;

import com.example.userlogin.dto.JwtAuthenticationResponse;
import com.example.userlogin.dto.LoginRequest;
import com.example.userlogin.dto.RefreshTokenRequest;
import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.service.JwtUserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * JWT Authentication Controller
 * 
 * This controller handles all JWT authentication endpoints:
 * - User registration
 * - User login 
 * - Token refresh
 * - Protected user and admin endpoints
 * 
 * All endpoints return JSON responses and use JWT tokens for authentication.
 */
@RestController
@RequestMapping("/api/auth")
@Profile("jwt")
public class JwtAuthController {

    private final JwtUserService jwtUserService;

    public JwtAuthController(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    /**
     * Register a new user
     * 
     * POST /api/auth/register
     * 
     * Request body: { "username", "email", "password" }
     * Response: JWT tokens and user info
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            JwtAuthenticationResponse response = jwtUserService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

    /**
     * Login user
     * 
     * POST /api/auth/login
     * 
     * Request body: { "username", "password" }
     * Response: JWT tokens and user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            JwtAuthenticationResponse response = jwtUserService.authenticateUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Login failed", e.getMessage()));
        }
    }

    /**
     * Refresh access token
     * 
     * POST /api/auth/refresh-token
     * 
     * Request body: { "refreshToken" }
     * Response: New JWT tokens and user info
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            JwtAuthenticationResponse response = jwtUserService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Token refresh failed", e.getMessage()));
        }
    }

    /**
     * Get user profile (requires USER or ADMIN role)
     * 
     * GET /api/auth/user/profile
     * 
     * Headers: Authorization: Bearer <access_token>
     * Response: User profile information
     */
    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            return ResponseEntity.ok(new ProfileResponse(
                username, 
                "User profile for: " + username + " - JWT Authentication",
                "USER"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Profile access failed", e.getMessage()));
        }
    }

    /**
     * Get admin dashboard (requires ADMIN role)
     * 
     * GET /api/auth/admin/dashboard
     * 
     * Headers: Authorization: Bearer <access_token>
     * Response: Admin dashboard information
     */
    @GetMapping("/admin/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            return ResponseEntity.ok(new ProfileResponse(
                username,
                "Admin dashboard for: " + username + " - JWT Authentication",
                "ADMIN"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Dashboard access failed", e.getMessage()));
        }
    }

    /**
     * Get current user info (requires authentication)
     * 
     * GET /api/auth/me
     * 
     * Headers: Authorization: Bearer <access_token>
     * Response: Current user information
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            String authorities = auth.getAuthorities().toString();
            
            return ResponseEntity.ok(new CurrentUserResponse(
                username,
                authorities,
                "Currently authenticated user via JWT"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("User info access failed", e.getMessage()));
        }
    }

    // Helper classes for responses

    /**
     * Error response for failed operations
     */
    public static class ErrorResponse {
        private String error;
        private String message;
        private long timestamp;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getError() { return error; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * Profile response for user/admin endpoints
     */
    public static class ProfileResponse {
        private String username;
        private String message;
        private String role;
        private long timestamp;

        public ProfileResponse(String username, String message, String role) {
            this.username = username;
            this.message = message;
            this.role = role;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getUsername() { return username; }
        public String getMessage() { return message; }
        public String getRole() { return role; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * Current user response for /me endpoint
     */
    public static class CurrentUserResponse {
        private String username;
        private String authorities;
        private String message;
        private long timestamp;

        public CurrentUserResponse(String username, String authorities, String message) {
            this.username = username;
            this.authorities = authorities;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getUsername() { return username; }
        public String getAuthorities() { return authorities; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}