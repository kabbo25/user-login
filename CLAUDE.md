# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a user login system project demonstrating 4 different authentication approaches:
1. **InMemory** - Users stored in memory (development/testing)
2. **JDBC** - Spring's built-in database authentication
3. **Custom JPA** - Custom entities with JPA
4. **JWT** - Stateless authentication with JSON Web Tokens

## Development Commands

### Running Different Profiles

```bash
# JWT Authentication (stateless, token-based)
mvn spring-boot:run -Dspring-boot.run.profiles=jwt

# Custom JPA Authentication (stateful, session-based)
mvn spring-boot:run -Dspring-boot.run.profiles=custom-jpa

# JDBC Authentication (stateful, session-based)
mvn spring-boot:run -Dspring-boot.run.profiles=jdbc

# InMemory Authentication (stateful, session-based)
mvn spring-boot:run -Dspring-boot.run.profiles=inmemory
```

### JWT Testing Commands

```bash
# Register new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "newuser", "email": "test@example.com", "password": "password123"}'

# Login user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "newuser", "password": "password123"}'

# Access protected endpoint (replace YOUR_TOKEN with actual token)
curl -X GET http://localhost:8080/api/auth/user/profile \
  -H "Authorization: Bearer YOUR_TOKEN"

# Refresh token
curl -X POST http://localhost:8080/api/auth/refresh-token \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "YOUR_REFRESH_TOKEN"}'
```

## Architecture

### JWT Authentication Flow
1. **Registration/Login** → Generate access & refresh tokens
2. **API Requests** → Include `Authorization: Bearer <token>` header
3. **Token Validation** → JwtAuthenticationFilter validates on each request
4. **Token Refresh** → Use refresh token to get new access token when expired

### Key Components

#### JWT Infrastructure
- `JwtUtil` - Token generation, validation, and parsing
- `JwtAuthenticationFilter` - Validates tokens on each request
- `JwtAuthenticationEntryPoint` - Handles unauthorized access
- `JwtSecurityConfig` - Security configuration for stateless authentication

#### Services & Controllers
- `JwtUserService` - Business logic for user operations and token management
- `JwtAuthController` - REST endpoints for authentication (`/api/auth/*`)

#### Token Configuration
- **Access Token**: 15 minutes (for API access)
- **Refresh Token**: 7 days (for getting new access tokens)
- **Algorithm**: HMAC-SHA256

### Database Schema
- Uses same User entity as Custom JPA approach
- Supports roles: USER, ADMIN
- No session storage (stateless)

## Basic Workflow
- Any file changes related to project (not documentation), use conventional commit and add the changes to git
- For JWT development, always test endpoints with proper Authorization headers
- Use Swagger UI at http://localhost:8080/swagger-ui/index.html for API testing