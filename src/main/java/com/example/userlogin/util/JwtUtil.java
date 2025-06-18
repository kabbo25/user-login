package com.example.userlogin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility Class
 * 
 * This class handles all JWT operations:
 * - Creating tokens
 * - Validating tokens
 * - Extracting information from tokens
 * 
 * Think of JWT tokens like special tickets:
 * - When you login, you get a ticket (token)
 * - The ticket has your info and expiration time
 * - You show this ticket to access protected areas
 */
@Component
public class JwtUtil {

    // Get JWT settings from application.yml file
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    /**
     * Extract username from JWT token
     * Like reading the name on a ticket
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from JWT token
     * Like checking when the ticket expires
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract any specific information from the token
     * This is a helper method that uses a function to get specific data
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all information from the token
     * Like opening the ticket and reading all the details
     */
    private Claims extractAllClaims(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check if the token has expired
     * Like checking if the ticket is still valid
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        Date now = new Date();
        return expiration.before(now);
    }

    /**
     * Generate an access token for a user
     * Like creating a new ticket with the user's name
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    /**
     * Generate a refresh token for a user
     * Like creating a longer-lasting ticket that can be used to get new tickets
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshTokenExpiration);
    }

    /**
     * Create the actual JWT token
     * This is like printing the ticket with all the necessary information
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        SecretKey key = getSigningKey();
        
        return Jwts.builder()
                .claims(claims)              // Extra information (like ticket type)
                .subject(subject)            // Username (like name on ticket)
                .issuedAt(now)              // When ticket was created
                .expiration(expiryDate)      // When ticket expires
                .signWith(key)              // Sign it with secret key (like official stamp)
                .compact();                 // Convert to string format
    }

    /**
     * Validate if the token is valid for a specific user
     * Like checking if the ticket belongs to the person and is not expired
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            String userDetailsUsername = userDetails.getUsername();
            
            // Check if username matches and token is not expired
            boolean usernameMatches = username.equals(userDetailsUsername);
            boolean tokenNotExpired = !isTokenExpired(token);
            
            return usernameMatches && tokenNotExpired;
        } catch (Exception e) {
            // If anything goes wrong, the token is invalid
            return false;
        }
    }

    /**
     * Check if token is valid (not expired and properly formatted)
     * Like doing a basic check if the ticket looks legitimate
     */
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the secret key used to sign tokens
     * Like getting the official stamp used to validate tickets
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}