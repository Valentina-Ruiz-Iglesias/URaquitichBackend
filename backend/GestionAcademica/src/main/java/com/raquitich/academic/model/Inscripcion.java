package com.raquitich.academic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones",
       uniqueConstraints = @UniqueConstraint(columnNames = {"seccion_id", "estudiante_username"}))
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(name = "estudiante_username", nullable = false, length = 100)
    private String estudianteUsername;

    @Column(name = "fecha_inscripcion", nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @Column(nullable = false)
    private boolean activa = true;

    @PrePersist
    protected void onCreate() {
        this.fechaInscripcion = LocalDateTime.now();
    }

    // Getters y Setters

    public Long getId() { return id; }

    public Seccion getSeccion() { return seccion; }
    public void setSeccion(Seccion seccion) { this.seccion = seccion; }

    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String estudianteUsername) { this.estudianteUsername = estudianteUsername; }

    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
