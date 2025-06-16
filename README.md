# Spring Security User Login System - 4 Implementation Approaches

This project demonstrates 4 different ways to implement user authentication in Spring Security:

1. **InMemoryUserDetailsService** - Users stored in memory
2. **JdbcUserDetailsManager** - Users stored in database with JDBC (Spring's built-in)
3. **Custom JPA UserDetailsService** - Users stored in database with custom JPA implementation
4. **Manual DaoAuthenticationProvider** - Custom authentication setup

## Quick Start

### 1. Build and Run

```bash
mvn clean install
```

### 2. Run with Different Profiles

#### InMemory Approach:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=inmemory
```

#### JDBC Approach:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=jdbc
```

#### Custom JPA Approach:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=custom-jpa
```

#### Manual Approach:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=manual
```

## API Endpoints

Each approach has its own set of endpoints:

### InMemory (`/api/inmemory/*`)
- `POST /api/inmemory/register` - Register new user
- `POST /api/inmemory/login` - Login user
- `GET /api/inmemory/user/profile` - User profile (requires USER role)
- `GET /api/inmemory/admin/dashboard` - Admin dashboard (requires ADMIN role)

### JDBC (`/api/jdbc/*`)
- `POST /api/jdbc/register` - Register new user
- `POST /api/jdbc/login` - Login user
- `GET /api/jdbc/user/profile` - User profile (requires USER role)
- `GET /api/jdbc/admin/dashboard` - Admin dashboard (requires ADMIN role)

### Custom JPA (`/api/custom-jpa/*`)
- `POST /api/custom-jpa/register` - Register new user
- `POST /api/custom-jpa/login` - Login user
- `GET /api/custom-jpa/user/profile` - User profile (requires USER role)
- `GET /api/custom-jpa/admin/dashboard` - Admin dashboard (requires ADMIN role)

### Manual (`/api/manual/*`)
- `POST /api/manual/register` - Register new user
- `POST /api/manual/login` - Login user
- `GET /api/manual/user/profile` - User profile (requires USER role)
- `GET /api/manual/admin/dashboard` - Admin dashboard (requires ADMIN role)

## Example Requests

### Registration
```bash
curl -X POST http://localhost:8080/api/jdbc/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/jdbc/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123"
  }'
```

### Access Protected Endpoint (Basic Auth)
```bash
curl -X GET http://localhost:8080/api/jdbc/user/profile \
  -u newuser:password123
```

## Pre-configured Test Users

### JDBC Profile
- **User**: `jdbcuser` / `password123` 
- **Admin**: `jdbcadmin` / `password123`

### Custom JPA & Manual Profiles  
- **User**: `testuser` / `password123`
- **Admin**: `testadmin` / `admin123`

## Key Differences Between Approaches

### 1. InMemoryUserDetailsService
- **Storage**: Users stored in application memory
- **Persistence**: No persistence, users lost on restart
- **Use Case**: Development, testing, simple applications
- **Configuration**: Uses Spring's built-in `InMemoryUserDetailsManager`
- **Registration**: Adds users to memory store

### 2. JdbcUserDetailsManager
- **Storage**: Users stored in database using Spring's standard JDBC approach
- **Persistence**: Full persistence with H2 database
- **Use Case**: Production applications that want to use Spring's built-in JDBC user management
- **Configuration**: Uses Spring's built-in `JdbcUserDetailsManager` with standard schema
- **Registration**: Uses JDBC templates with Spring Security's standard tables (`users`, `authorities`)
- **Schema**: Requires Spring Security's standard database schema

### 3. Custom JPA UserDetailsService
- **Storage**: Users stored in database via JPA with custom entities
- **Persistence**: Full persistence with H2 database
- **Use Case**: Production applications with custom user entities and complex user data
- **Configuration**: Custom `UserDetailsService` implementing database lookup via JPA
- **Registration**: Saves users to database via JPA repository with custom User entity

### 4. Manual DaoAuthenticationProvider
- **Storage**: Users stored in database via JPA
- **Persistence**: Full persistence with H2 database
- **Use Case**: When you need full control over authentication process
- **Configuration**: Manually configured `DaoAuthenticationProvider` and `AuthenticationManager`
- **Registration**: Same as Custom JPA but with explicit authentication provider setup

## Architecture Comparison

| Feature | InMemory | JDBC | Custom JPA | Manual |
|---------|----------|------|------------|--------|
| User Storage | Memory | Database (JDBC) | Database (JPA) | Database (JPA) |
| Persistence | No | Yes | Yes | Yes |
| UserDetailsService | Built-in | Built-in | Custom | Custom |
| AuthenticationProvider | Auto-configured | Auto-configured | Auto-configured | Manual |
| AuthenticationManager | Auto-configured | Auto-configured | Auto-configured | Manual |
| Schema | N/A | Spring Standard | Custom | Custom |
| Complexity | Low | Low-Medium | Medium | High |
| Control Level | Low | Medium | Medium-High | High |

## Security Configuration Highlights

### InMemory
```java
@Bean
public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(users);
}
```

### JDBC
```java
@Bean
public UserDetailsService jdbcUserDetailsService() {
    return new JdbcUserDetailsManager(dataSource);
}
```

### Custom JPA
```java
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}
```

### Manual
```java
@Bean
public AuthenticationManager manualAuthenticationManager() {
    return new ProviderManager(Collections.singletonList(manualAuthenticationProvider()));
}
```

## Database Access

When running with `jdbc`, `custom-jpa`, or `manual` profiles, you can access the H2 console at:
http://localhost:8080/h2-console

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa` 
- **Password**: `password`

### JDBC Profile Tables
- `users` - Standard Spring Security users table
- `authorities` - Standard Spring Security authorities table  
- `user_profiles` - Additional profile information

### Custom JPA & Manual Profile Tables
- `users` - Custom User entity table with id, username, email, password, role