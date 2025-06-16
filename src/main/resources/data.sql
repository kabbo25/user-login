-- Initial data for JdbcUserDetailsManager
-- Password is BCrypt encoded version of 'password123' and 'admin123'

INSERT INTO users (username, password, enabled) VALUES 
('jdbcuser', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true),
('jdbcadmin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true);

INSERT INTO authorities (username, authority) VALUES 
('jdbcuser', 'ROLE_USER'),
('jdbcadmin', 'ROLE_USER'),
('jdbcadmin', 'ROLE_ADMIN');

INSERT INTO user_profiles (username, email) VALUES 
('jdbcuser', 'jdbcuser@example.com'),
('jdbcadmin', 'jdbcadmin@example.com');