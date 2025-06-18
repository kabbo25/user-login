package com.example.userlogin.dto;

/**
 * JWT Authentication Response
 * 
 * This class represents the response we send back when someone successfully logs in.
 * It contains:
 * - Access token (short-lived, for accessing protected resources)
 * - Refresh token (long-lived, for getting new access tokens)
 * - User information
 * - Token expiration times
 * 
 * Think of it like a complete login package with everything the user needs.
 */
public class JwtAuthenticationResponse {
    
    // The main token used to access protected endpoints
    private String accessToken;
    
    // Token used to get new access tokens when they expire
    private String refreshToken;
    
    // Type of token (usually "Bearer")
    private String tokenType = "Bearer";
    
    // How long the access token is valid (in seconds)
    private long accessTokenExpiresIn;
    
    // How long the refresh token is valid (in seconds)
    private long refreshTokenExpiresIn;
    
    // Username of the logged-in user
    private String username;
    
    // User's role (USER, ADMIN, etc.)
    private String role;

    // Default constructor (needed for JSON processing)
    public JwtAuthenticationResponse() {}

    // Constructor with all the important information
    public JwtAuthenticationResponse(String accessToken, String refreshToken, 
                                   long accessTokenExpiresIn, long refreshTokenExpiresIn,
                                   String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    // These allow other parts of the application to read and modify the values

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getAccessTokenExpiresIn() {
        return accessTokenExpiresIn;
    }

    public void setAccessTokenExpiresIn(long accessTokenExpiresIn) {
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(long refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}