package com.example.userlogin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point
 * 
 * This class handles what happens when someone tries to access a protected endpoint
 * without being authenticated (no valid JWT token).
 * 
 * Think of it like a bouncer at a club - if you don't have a valid ticket (JWT token),
 * this class decides what response you get.
 * 
 * Instead of redirecting to a login page (like traditional web apps),
 * we return a JSON error response since this is a REST API.
 */
@Component
@Profile("jwt")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        // Set the response status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // Set the response content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Create an error response with helpful information
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", "Access denied. Please provide a valid JWT token.");
        errorResponse.put("status", 401);
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("timestamp", System.currentTimeMillis());
        
        // Convert the error response to JSON and write it to the response
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        
        response.getWriter().write(jsonResponse);
    }
}