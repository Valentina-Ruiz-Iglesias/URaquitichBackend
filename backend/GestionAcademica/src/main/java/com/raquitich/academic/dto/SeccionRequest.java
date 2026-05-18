package com.raquitich.academic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SeccionRequest {

    @NotNull(message = "El ID de asignatura es obligatorio")
    private Long asignaturaId;

    @NotBlank(message = "El periodo es obligatorio")
    private String periodo;

    private Integer cupoMaximo = 40;

    @NotBlank(message = "El docente es obligatorio")
    private String docenteUsername;

    public Long getAsignaturaId() { return asignaturaId; }
    public void setAsignaturaId(Long asignaturaId) { this.asignaturaId = asignaturaId; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Integer getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(Integer cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public String getDocenteUsername() { return docenteUsername; }
    public void setDocenteUsername(String docenteUsername) { this.docenteUsername = docenteUsername; }
}
