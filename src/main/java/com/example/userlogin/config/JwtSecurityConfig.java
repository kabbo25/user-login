package com.example.userlogin.config;

import com.example.userlogin.security.JwtAuthenticationEntryPoint;
import com.example.userlogin.security.JwtAuthenticationFilter;
import com.example.userlogin.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT Security Configuration
 * 
 * This class sets up the security configuration for JWT-based authentication.
 * 
 * Key differences from traditional session-based security:
 * 1. No sessions - we use tokens instead
 * 2. Custom JWT filter to validate tokens on each request
 * 3. Custom entry point to handle unauthorized access
 * 4. CORS configuration for frontend integration
 */
@Configuration
@EnableWebSecurity
@Profile("jwt")
public class JwtSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtSecurityConfig(CustomUserDetailsService userDetailsService,
                           JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                           JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Password encoder for encrypting passwords
     * Same as other configurations - we use BCrypt for security
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider that knows how to validate users
     * Links our custom user service with password encoding
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication manager - handles the actual authentication process
     * We need this for login (when checking username/password)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Main security configuration
     * This is where we set up all the security rules
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Use our custom authentication provider
                .authenticationProvider(authenticationProvider())
                
                // Disable CSRF - not needed for stateless JWT APIs
                .csrf(AbstractHttpConfigurer::disable)
                
                // Set up URL access rules
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - anyone can access these
                        .requestMatchers(
                            "/api/auth/register",
                            "/api/auth/login", 
                            "/api/auth/refresh-token",
                            "/swagger-ui/**", 
                            "/v3/api-docs/**"
                        ).permitAll()
                        
                        // Admin endpoints - only ADMIN role can access
                        .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")
                        
                        // User endpoints - USER or ADMIN role can access
                        .requestMatchers("/api/auth/user/**").hasAnyRole("USER", "ADMIN")
                        
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                
                // Configure exception handling
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                
                // Configure session management
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add our JWT filter before the standard authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}