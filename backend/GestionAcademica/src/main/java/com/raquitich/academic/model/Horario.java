package com.raquitich.academic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @Column(name = "dia_semana", nullable = false, length = 10)
    private String diaSemana;   // LUNES, MARTES, ...

    @Column(name = "hora_inicio", nullable = false, length = 8)
    private String horaInicio;  // "08:00"

    @Column(name = "hora_fin", nullable = false, length = 8)
    private String horaFin;     // "09:30"

    @Column(length = 50)
    private String sala;

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Seccion getSeccion() { return seccion; }
    public void setSeccion(Seccion seccion) { this.seccion = seccion; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }
}
