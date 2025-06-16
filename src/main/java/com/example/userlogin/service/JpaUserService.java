package com.example.userlogin.service;

import com.example.userlogin.dto.RegistrationRequest;
import com.example.userlogin.entity.User;
import com.example.userlogin.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("custom-jpa")
public class JpaUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public boolean authenticateUser(String username, String password) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return false;
            }
            return passwordEncoder.matches(password, user.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}