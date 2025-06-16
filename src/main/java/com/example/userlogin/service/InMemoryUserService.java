package com.example.userlogin.service;

import com.example.userlogin.dto.RegistrationRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@Profile("inmemory")
public class InMemoryUserService {
    
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public InMemoryUserService(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(RegistrationRequest request) {
        try {
            if (userDetailsManager.userExists(request.getUsername())) {
                return false;
            }

            UserDetails user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles("USER")
                    .build();

            userDetailsManager.createUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            if (!userDetailsManager.userExists(username)) {
                return false;
            }
            
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
            return passwordEncoder.matches(password, userDetails.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}