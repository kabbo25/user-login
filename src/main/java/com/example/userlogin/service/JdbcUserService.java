package com.example.userlogin.service;

import com.example.userlogin.dto.RegistrationRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("jdbc")
public class JdbcUserService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserService(JdbcUserDetailsManager jdbcUserDetailsManager, 
                          PasswordEncoder passwordEncoder, 
                          JdbcTemplate jdbcTemplate) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public boolean registerUser(RegistrationRequest request) {
        try {
            // Check if user already exists
            if (jdbcUserDetailsManager.userExists(request.getUsername())) {
                return false;
            }
            
            // Check if email already exists
            int emailCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user_profiles WHERE email = ?", 
                Integer.class, 
                request.getEmail()
            );
            if (emailCount > 0) {
                return false;
            }

            // Create UserDetails object
            UserDetails user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .authorities("ROLE_USER")
                    .build();

            // Create user in Spring Security tables
            jdbcUserDetailsManager.createUser(user);
            
            // Store additional profile information
            jdbcTemplate.update(
                "INSERT INTO user_profiles (username, email) VALUES (?, ?)",
                request.getUsername(),
                request.getEmail()
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            if (!jdbcUserDetailsManager.userExists(username)) {
                return false;
            }
            
            UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(username);
            return passwordEncoder.matches(password, userDetails.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}