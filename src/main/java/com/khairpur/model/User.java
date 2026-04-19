package com.khairpur.model;

/**
 * Represents a user (admin or regular user) in the system.
 */
public class User extends Person {
    private String username;
    private String password;
    private String email;
    private String role; // "admin" or "user"

    public User() {}

    public User(int id, String username, String password, String role,
                String name, String email, String phone) {
        super(id, name, phone);
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }

    @Override
    public String toString() { return name + " (" + username + ")"; }
}
