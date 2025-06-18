# JWT-Based Authentication System Plan

## Overview
Transform the existing Spring Security authentication system to use JWT tokens instead of session-based authentication.

## Implementation Steps

### 1. Add JWT Dependencies
- Add `jjwt-api`, `jjwt-impl`, and `jjwt-jackson` dependencies to `pom.xml`
- Add JWT configuration properties to `application.yml`

### 2. Create JWT Infrastructure
- Create `JwtUtil` class for token generation, validation, and parsing
- Create `JwtAuthenticationFilter` to intercept requests and validate JWT tokens
- Create `JwtAuthenticationEntryPoint` for handling unauthorized access

### 3. Update DTOs
- Modify `AuthResponse` to include JWT token
- Add `JwtAuthenticationResponse` with token, expiration, and user details
- Update existing DTOs to support JWT workflow

### 4. Create JWT Security Configuration
- Create `JwtSecurityConfig` class extending `WebSecurityConfigurerAdapter`
- Configure JWT filter chain and disable session management
- Set up CORS and CSRF configurations for JWT

### 5. Update Controllers
- Create new `JwtAuthController` with `/api/auth` endpoints
- Implement `/login` endpoint returning JWT token
- Implement `/register` endpoint with JWT token response
- Add `/refresh-token` endpoint for token renewal
- Add protected endpoints demonstrating JWT usage

### 6. Create JWT User Service
- Create `JwtUserService` for user management with JWT
- Implement user registration and authentication logic
- Add token refresh functionality

### 7. Testing & Validation
- Create integration tests for JWT endpoints
- Test token validation, expiration, and refresh
- Verify protected endpoint access with valid/invalid tokens

### 8. Documentation
- Update `CLAUDE.md` with JWT implementation details
- Add API documentation for new endpoints
- Include JWT usage examples and best practices

## Files to Create/Modify
- **New:** `JwtUtil.java`, `JwtAuthenticationFilter.java`, `JwtAuthenticationEntryPoint.java`
- **New:** `JwtSecurityConfig.java`, `JwtAuthController.java`, `JwtUserService.java`
- **New:** `JwtAuthenticationResponse.java`
- **Update:** `pom.xml`, `application.yml`, `AuthResponse.java`, `CLAUDE.md`

## Key Features
- Stateless authentication using JWT tokens
- Token expiration and refresh mechanism
- Role-based access control preserved
- Secure token signing with configurable secret
- Comprehensive error handling and validation

## Implementation Notes
- Use HMAC-SHA256 for token signing
- Set reasonable token expiration times (15 minutes for access tokens, 7 days for refresh tokens)
- Implement proper token validation and error handling
- Ensure secure storage of JWT secret key
- Add comprehensive logging for security events