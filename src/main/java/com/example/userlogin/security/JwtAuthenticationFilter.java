package com.example.userlogin.security;

import com.example.userlogin.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * 
 * This filter runs on every request to check if the user has a valid JWT token.
 * Think of it like a security guard that checks tickets at the entrance.
 * 
 * Here's what it does:
 * 1. Look for the JWT token in the request header
 * 2. If found, validate the token
 * 3. If valid, authenticate the user for this request
 * 4. If not valid or missing, let the request continue (other security will handle it)
 */
@Component
@Profile("jwt")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Step 1: Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwtToken = null;

        // Step 2: Check if the header contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token (remove "Bearer " prefix)
            jwtToken = authorizationHeader.substring(7);
            
            try {
                // Step 3: Extract username from the token
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                // If token is malformed, we'll just continue without authentication
                logger.warn("JWT token is malformed: " + e.getMessage());
            }
        }

        // Step 4: If we have a username and no one is currently authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // Step 5: Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Step 6: Validate the token against the user details
                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    
                    // Step 7: Create authentication token
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    // Step 8: Set additional details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Step 9: Set the authentication in SecurityContext
                    // This tells Spring Security that the user is authenticated for this request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // If anything goes wrong, log it and continue without authentication
                logger.warn("Cannot set user authentication: " + e.getMessage());
            }
        }

        // Step 10: Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}