package com.example.userlogin.service;

import com.example.userlogin.dto.JwtAuthenticationResponse;
import com.example.userlogin.dto.LoginRequest;
import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.entity.User;
import com.example.userlogin.repository.UserRepository;
import com.example.userlogin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * JWT User Service
 * 
 * This service handles user operations for JWT authentication:
 * - User registration
 * - User authentication and token generation
 * - Token refresh
 * 
 * Think of this as the "business logic" layer that coordinates between
 * the controller (web layer) and the database (data layer).
 */
@Service
@Profile("jwt")
public class JwtUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Get token expiration times from configuration
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public JwtUserService(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil,
                         UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Register a new user
     * 
     * Steps:
     * 1. Check if username or email already exists
     * 2. Create new user with encoded password
     * 3. Save to database
     * 4. Generate JWT tokens
     * 5. Return authentication response
     */
    public JwtAuthenticationResponse registerUser(RegistrationRequest request) {
        // Step 1: Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Step 2: Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER); // Default role

        // Step 3: Save user to database
        User savedUser = userRepository.save(user);

        // Step 4: Generate tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // Step 5: Return response with tokens
        return new JwtAuthenticationResponse(
            accessToken,
            refreshToken,
            accessTokenExpiration / 1000, // Convert to seconds
            refreshTokenExpiration / 1000, // Convert to seconds
            savedUser.getUsername(),
            savedUser.getRole().name()
        );
    }

    /**
     * Authenticate user and generate tokens
     * 
     * Steps:
     * 1. Authenticate username and password
     * 2. If successful, load user details
     * 3. Generate JWT tokens
     * 4. Return authentication response
     */
    public JwtAuthenticationResponse authenticateUser(LoginRequest request) {
        try {
            // Step 1: Authenticate the user
            String username = request.getUsername();
            String password = request.getPassword();
            
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(username, password);
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            // Step 2: Load user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Step 3: Get user info from database
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Step 4: Generate tokens
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            // Step 5: Return response with tokens
            return new JwtAuthenticationResponse(
                accessToken,
                refreshToken,
                accessTokenExpiration / 1000, // Convert to seconds
                refreshTokenExpiration / 1000, // Convert to seconds
                user.getUsername(),
                user.getRole().name()
            );
            
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    /**
     * Refresh access token using refresh token
     * 
     * Steps:
     * 1. Validate the refresh token
     * 2. Extract username from token
     * 3. Load user details
     * 4. Generate new access token
     * 5. Return new authentication response
     */
    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        try {
            // Step 1: Check if refresh token is valid
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }
            
            // Step 2: Extract username from refresh token
            String username = jwtUtil.extractUsername(refreshToken);
            
            // Step 3: Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Step 4: Validate token against user details
            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                throw new RuntimeException("Refresh token validation failed");
            }
            
            // Step 5: Get user info from database
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Step 6: Generate new tokens
            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            // Step 7: Return response with new tokens
            return new JwtAuthenticationResponse(
                newAccessToken,
                newRefreshToken,
                accessTokenExpiration / 1000, // Convert to seconds
                refreshTokenExpiration / 1000, // Convert to seconds
                user.getUsername(),
                user.getRole().name()
            );
            
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }
    }
}