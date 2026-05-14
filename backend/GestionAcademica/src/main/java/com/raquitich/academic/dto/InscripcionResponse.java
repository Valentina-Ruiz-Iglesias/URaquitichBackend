package com.raquitich.academic.dto;

import com.raquitich.academic.model.Inscripcion;

import java.time.LocalDateTime;

public class InscripcionResponse {

    private Long id;
    private Long seccionId;
    private String estudianteUsername;
    private LocalDateTime fechaInscripcion;
    private boolean activa;

    public InscripcionResponse() {}

    public static InscripcionResponse from(Inscripcion i) {
        InscripcionResponse r = new InscripcionResponse();
        r.id                  = i.getId();
        r.seccionId           = i.getSeccion().getId();
        r.estudianteUsername  = i.getEstudianteUsername();
        r.fechaInscripcion    = i.getFechaInscripcion();
        r.activa              = i.isActiva();
        return r;
    }

    public Long getId() { return id; }
    public Long getSeccionId() { return seccionId; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public boolean isActiva() { return activa; }
}
