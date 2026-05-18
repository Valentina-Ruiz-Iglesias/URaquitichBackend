package com.raquitich.auth.dto;

public class ChangeRoleRequest {

    private String role;   // ROLE_ESTUDIANTE, ROLE_DOCENTE, ROLE_DIRECTIVO, ROLE_ADMIN

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
