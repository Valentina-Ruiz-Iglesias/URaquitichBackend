package com.raquitich.student.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO usado cuando el directivo/admin quiere EDITAR un estudiante que ya existe.
 *
 * ¿Por qué no tiene username, email ni password?
 *   - El username no se puede cambiar porque es el identificador en ServicioAutenticacion.
 *   - El email y la contraseña viven en ServicioAutenticacion, no aquí.
 *   - Solo actualizamos los datos de perfil que pertenecen a esta base de datos.
 */
public class EstudianteUpdateRequest {

    // Nombre completo — obligatorio, entre 2 y 100 caracteres
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 12, message = "El RUT no puede superar los 12 caracteres")
    private String rut;

    // Fecha de nacimiento — opcional, puede ser null
    private LocalDate fechaNacimiento;

    // Número de matrícula — opcional, máximo 20 caracteres
    @Size(max = 20, message = "El número de matrícula no puede superar los 20 caracteres")
    private String numeroMatricula;

    // Carrera o curso — opcional, máximo 100 caracteres
    @Size(max = 100, message = "La carrera no puede superar los 100 caracteres")
    private String carrera;

    // Año de ingreso — opcional, debe estar entre 2000 y 2100
    @Min(value = 2000, message = "El año de ingreso no es válido")
    @Max(value = 2100, message = "El año de ingreso no es válido")
    private Integer anioIngreso;

    // ---- Getters y Setters ----
    // Angular los necesita para serializar/deserializar el JSON

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNumeroMatricula() { return numeroMatricula; }
    public void setNumeroMatricula(String numeroMatricula) { this.numeroMatricula = numeroMatricula; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getAnioIngreso() { return anioIngreso; }
    public void setAnioIngreso(Integer anioIngreso) { this.anioIngreso = anioIngreso; }
}
