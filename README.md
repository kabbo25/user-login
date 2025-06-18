# Spring Security User Login System - 3 Implementation Approaches

This project demonstrates 3 different ways to implement user authentication in Spring Security:

1. **InMemoryUserDetailsService** - Users stored in memory
2. **JdbcUserDetailsManager** - Users stored in database with JDBC (Spring's built-in)
3. **Custom JPA UserDetailsService** - Users stored in database with custom JPA implementation

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


## Example Requests

### Registration
```bash
curl -X POST http://local"password": "password123"
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

### Custom JPA Profile  
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


## Architecture Comparison

| Feature | InMemory | JDBC | Custom JPA |
|---------|----------|------|------------|
| User Storage | Memory | Database (JDBC) | Database (JPA) |
| Persistence | No | Yes | Yes |
| UserDetailsService | Built-in | Built-in | Custom |
| AuthenticationProvider | Auto-configured | Auto-configured | Auto-configured |
| AuthenticationManager | Auto-configured | Auto-configured | Auto-configured |
| Schema | N/A | Spring Standard | Custom |
| Complexity | Low | Low-Medium | Medium |
| Control Level | Low | Medium | Medium-High |

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


## Database Access

When running with `jdbc` or `custom-jpa` profiles, you can access the H2 console at:
http://localhost:8080/h2-console

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa` 
- **Password**: `password`

### JDBC Profile Tables
- `users` - Standard Spring Security users table
- `authorities` - Standard Spring Security authorities table  
- `user_profiles` - Additional profile information

### Custom JPA Profile Tables
- `users` - Custom User entity table with id, username, email, password, role


# diagram of spring security flow
![Mermaid Chart - Create complex, visual diagrams with text. A smarter way of creating diagrams.-2025-06-18-080441.png](Mermaid%20Chart%20-%20Create%20complex%2C%20visual%20diagrams%20with%20text.%20A%20smarter%20way%20of%20creating%20diagrams.-2025-06-18-080441.png)