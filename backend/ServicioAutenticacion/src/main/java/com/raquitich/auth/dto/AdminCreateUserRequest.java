package com.raquitich.auth.dto;

public class AdminCreateUserRequest {

    private String username;
    private String email;
    private String nombre;
    private String password;
    private String role;   // ROLE_ESTUDIANTE, ROLE_DOCENTE, ROLE_DIRECTIVO, ROLE_ADMIN

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
