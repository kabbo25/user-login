package com.example.userlogin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Refresh Token Request
 * 
 * This class represents a request to get a new access token
 * using a refresh token.
 * 
 * When access tokens expire (after 15 minutes), instead of 
 * asking the user to login again, we can use the refresh token
 * to get a new access token.
 */
public class RefreshTokenRequest {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    // Default constructor
    public RefreshTokenRequest() {}

    // Constructor with refresh token
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter and Setter
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}