package com.ingeduardo.demostore.dto;

import java.util.List;

import com.ingeduardo.demostore.model.enums.RoleName;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private List<RoleName> roles;

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<RoleName> getRoles() { return roles; }
    public void setRoles(List<RoleName> roles) { this.roles = roles; }
}
