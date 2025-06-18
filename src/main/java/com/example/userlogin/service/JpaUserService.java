package com.example.userlogin.service;

import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.dto.LoginRequest;
import com.example.userlogin.entity.User;
import com.example.userlogin.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("custom-jpa")
public class JpaUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JpaUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public boolean registerUser(RegistrationRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername()) || 
                userRepository.existsByEmail(request.getEmail())) {
                return false;
            }

            User user = new User(
                    request.getUsername(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean authenticateUser(LoginRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            
            Authentication authentication = authenticationManager.authenticate(authToken);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

}