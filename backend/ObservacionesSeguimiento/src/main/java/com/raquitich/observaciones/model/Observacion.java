package com.raquitich.observaciones.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "observaciones")
public class Observacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_username", nullable = false, length = 50)
    private String estudianteUsername;

    @Column(name = "autor_username", nullable = false, length = 50)
    private String autorUsername;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoObservacion tipo;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_observacion", nullable = false)
    private LocalDate fechaObservacion;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        if (this.fechaObservacion == null) {
            this.fechaObservacion = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }

    // Getters y Setters

    public Long getId() { return id; }

    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String v) { this.estudianteUsername = v; }

    public String getAutorUsername() { return autorUsername; }
    public void setAutorUsername(String v) { this.autorUsername = v; }

    public TipoObservacion getTipo() { return tipo; }
    public void setTipo(TipoObservacion v) { this.tipo = v; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String v) { this.titulo = v; }

    public String getContenido() { return contenido; }
    public void setContenido(String v) { this.contenido = v; }

    public LocalDate getFechaObservacion() { return fechaObservacion; }
    public void setFechaObservacion(LocalDate v) { this.fechaObservacion = v; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
}
