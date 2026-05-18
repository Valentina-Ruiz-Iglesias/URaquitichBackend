package com.raquitich.calendario.dto;

import com.raquitich.calendario.model.Evento;

public class EventoResponse {

    private Long id;
    private String tipo;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private String creadoPor;
    private String estudianteUsername;
    private String color;
    private String creadoEn;

    public static EventoResponse from(Evento e) {
        EventoResponse r = new EventoResponse();
        r.id                  = e.getId();
        r.tipo                = e.getTipo();
        r.titulo              = e.getTitulo();
        r.descripcion         = e.getDescripcion();
        r.fechaInicio         = e.getFechaInicio() != null ? e.getFechaInicio().toString() : null;
        r.fechaFin            = e.getFechaFin()    != null ? e.getFechaFin().toString()    : null;
        r.horaInicio          = e.getHoraInicio();
        r.horaFin             = e.getHoraFin();
        r.creadoPor           = e.getCreadoPor();
        r.estudianteUsername  = e.getEstudianteUsername();
        r.color               = e.getColor();
        r.creadoEn            = e.getCreadoEn() != null ? e.getCreadoEn().toString() : null;
        return r;
    }

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getCreadoPor() { return creadoPor; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public String getColor() { return color; }
    public String getCreadoEn() { return creadoEn; }
}
