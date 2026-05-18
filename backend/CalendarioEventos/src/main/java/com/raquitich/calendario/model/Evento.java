package com.raquitich.calendario.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** INSTITUCIONAL (creado por directivo, visible a todos) o PERSONAL (creado por estudiante) */
    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    /** Fecha de inicio del evento */
    @Column(nullable = false)
    private LocalDate fechaInicio;

    /** Fecha de fin (puede ser igual a fechaInicio para eventos de un día) */
    private LocalDate fechaFin;

    /** Hora de inicio opcional, formato HH:mm */
    private String horaInicio;

    /** Hora de fin opcional, formato HH:mm */
    private String horaFin;

    /** Username del usuario que creó el evento */
    @Column(nullable = false)
    private String creadoPor;

    /** Solo para eventos PERSONAL: username del estudiante dueño */
    private String estudianteUsername;

    /** Color para mostrar en el calendario (hex o nombre) */
    private String color;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String estudianteUsername) { this.estudianteUsername = estudianteUsername; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
