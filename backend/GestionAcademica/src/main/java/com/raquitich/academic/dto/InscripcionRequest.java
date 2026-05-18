package com.raquitich.academic.dto;

import jakarta.validation.constraints.NotBlank;

public class InscripcionRequest {

    @NotBlank(message = "El username del estudiante es obligatorio")
    private String estudianteUsername;

    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String estudianteUsername) { this.estudianteUsername = estudianteUsername; }
}
