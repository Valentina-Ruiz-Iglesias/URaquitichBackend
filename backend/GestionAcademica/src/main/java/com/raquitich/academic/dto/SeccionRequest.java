package com.raquitich.academic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SeccionRequest {

    @NotNull(message = "La asignatura es obligatoria")
    private Long asignaturaId;

    @NotBlank(message = "El período es obligatorio")
    private String periodo;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo mínimo es 1")
    private Integer cupoMaximo;

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
