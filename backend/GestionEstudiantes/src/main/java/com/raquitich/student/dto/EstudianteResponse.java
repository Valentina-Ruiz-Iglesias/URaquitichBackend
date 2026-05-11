package com.raquitich.student.dto;

import com.raquitich.student.model.Estudiante;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EstudianteResponse {

    private Long id;
    private String username;
    private String email;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String numeroMatricula;
    private String carrera;
    private Integer anioIngreso;
    private boolean activo;
    private LocalDateTime creadoEn;

    public EstudianteResponse(Estudiante e) {
        this.id              = e.getId();
        this.username        = e.getUsername();
        this.email           = e.getEmail();
        this.nombre          = e.getNombre();
        this.fechaNacimiento = e.getFechaNacimiento();
        this.numeroMatricula = e.getNumeroMatricula();
        this.carrera         = e.getCarrera();
        this.anioIngreso     = e.getAnioIngreso();
        this.activo          = e.isActivo();
        this.creadoEn        = e.getCreadoEn();
    }

    // Getters

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getNumeroMatricula() { return numeroMatricula; }
    public String getCarrera() { return carrera; }
    public Integer getAnioIngreso() { return anioIngreso; }
    public boolean isActivo() { return activo; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
