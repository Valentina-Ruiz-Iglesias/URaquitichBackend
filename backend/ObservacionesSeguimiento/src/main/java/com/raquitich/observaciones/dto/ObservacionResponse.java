package com.raquitich.observaciones.dto;

import com.raquitich.observaciones.model.Observacion;
import com.raquitich.observaciones.model.TipoObservacion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ObservacionResponse {

    private Long id;
    private String estudianteUsername;
    private String autorUsername;
    private TipoObservacion tipo;
    private String titulo;
    private String contenido;
    private LocalDate fechaObservacion;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    public ObservacionResponse(Observacion o) {
        this.id                  = o.getId();
        this.estudianteUsername  = o.getEstudianteUsername();
        this.autorUsername       = o.getAutorUsername();
        this.tipo                = o.getTipo();
        this.titulo              = o.getTitulo();
        this.contenido           = o.getContenido();
        this.fechaObservacion    = o.getFechaObservacion();
        this.creadoEn            = o.getCreadoEn();
        this.actualizadoEn       = o.getActualizadoEn();
    }

    public Long getId() { return id; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public String getAutorUsername() { return autorUsername; }
    public TipoObservacion getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public LocalDate getFechaObservacion() { return fechaObservacion; }
    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
}
