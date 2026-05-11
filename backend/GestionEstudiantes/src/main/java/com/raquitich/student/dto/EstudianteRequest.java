package com.raquitich.student.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EstudianteRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    private LocalDate fechaNacimiento;

    @Size(max = 20, message = "El número de matrícula no puede superar los 20 caracteres")
    private String numeroMatricula;

    @Size(max = 100, message = "La carrera no puede superar los 100 caracteres")
    private String carrera;

    @Min(value = 2000, message = "El año de ingreso no es válido")
    @Max(value = 2100, message = "El año de ingreso no es válido")
    private Integer anioIngreso;

    // Getters y Setters

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNumeroMatricula() { return numeroMatricula; }
    public void setNumeroMatricula(String numeroMatricula) { this.numeroMatricula = numeroMatricula; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getAnioIngreso() { return anioIngreso; }
    public void setAnioIngreso(Integer anioIngreso) { this.anioIngreso = anioIngreso; }
}
