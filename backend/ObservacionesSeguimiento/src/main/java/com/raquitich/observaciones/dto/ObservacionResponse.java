package com.raquitich.observaciones.dto;

import com.raquitich.observaciones.model.Observacion;

public class ObservacionResponse {

    private Long id;
    private String estudianteUsername;
    private String docenteUsername;
    private String tipo;
    private String titulo;
    private String contenido;
    private String fechaObservacion;   // String para evitar problemas de serialización Jackson
    private String creadoEn;

    public static ObservacionResponse from(Observacion obs) {
        ObservacionResponse r = new ObservacionResponse();
        r.id                  = obs.getId();
        r.estudianteUsername  = obs.getEstudianteUsername();
        r.docenteUsername     = obs.getDocenteUsername();
        r.tipo                = obs.getTipo();
        r.titulo              = obs.getTitulo();
        r.contenido           = obs.getContenido();
        r.fechaObservacion    = obs.getFechaObservacion() != null
                                ? obs.getFechaObservacion().toString() : null;
        r.creadoEn            = obs.getCreadoEn() != null
                                ? obs.getCreadoEn().toString() : null;
        return r;
    }

    public Long getId() { return id; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public String getDocenteUsername() { return docenteUsername; }
    public String getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public String getFechaObservacion() { return fechaObservacion; }
    public String getCreadoEn() { return creadoEn; }
}
