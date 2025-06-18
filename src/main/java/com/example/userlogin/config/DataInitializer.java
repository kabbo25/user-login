package com.example.userlogin.config;

import com.example.userlogin.entity.User;
import com.example.userlogin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    @Profile({"custom-jpa", "jwt"})
    public CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                User user = new User("testuser", "user@example.com", passwordEncoder.encode("password123"));
                User admin = new User("testadmin", "admin@example.com", passwordEncoder.encode("admin123"), User.Role.ADMIN);
                
                repository.save(user);
                repository.save(admin);
                
                System.out.println("Initialized database with test users:");
                System.out.println("User: testuser / password123");
                System.out.println("Admin: testadmin / admin123");
            }
        };
    }
}