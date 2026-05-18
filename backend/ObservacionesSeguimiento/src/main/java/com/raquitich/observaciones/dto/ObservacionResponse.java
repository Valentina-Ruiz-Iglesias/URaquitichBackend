package com.raquitich.observaciones.dto;

import com.raquitich.observaciones.model.Observacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ObservacionResponse {

    private Long id;
    private String estudianteUsername;
    private String docenteUsername;
    private String tipo;
    private String titulo;
    private String contenido;
    private LocalDate fechaObservacion;
    private LocalDateTime creadoEn;

    public static ObservacionResponse from(Observacion obs) {
        ObservacionResponse r = new ObservacionResponse();
        r.id                   = obs.getId();
        r.estudianteUsername   = obs.getEstudianteUsername();
        r.docenteUsername      = obs.getDocenteUsername();
        r.tipo                 = obs.getTipo();
        r.titulo               = obs.getTitulo();
        r.contenido            = obs.getContenido();
        r.fechaObservacion     = obs.getFechaObservacion();
        r.creadoEn             = obs.getCreadoEn();
        return r;
    }

    public Long getId() { return id; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public String getDocenteUsername() { return docenteUsername; }
    public String getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public LocalDate getFechaObservacion() { return fechaObservacion; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
}
