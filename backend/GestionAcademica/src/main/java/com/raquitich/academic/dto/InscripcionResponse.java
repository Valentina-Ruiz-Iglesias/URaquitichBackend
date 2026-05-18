package com.raquitich.academic.dto;

import com.raquitich.academic.model.Inscripcion;

public class InscripcionResponse {

    private Long id;
    private Long seccionId;
    private String estudianteUsername;
    private String fechaInscripcion;
    private boolean activa;

    public static InscripcionResponse from(Inscripcion i) {
        InscripcionResponse dto = new InscripcionResponse();
        dto.id                  = i.getId();
        dto.seccionId           = i.getSeccion().getId();
        dto.estudianteUsername  = i.getEstudianteUsername();
        dto.fechaInscripcion    = i.getFechaInscripcion() != null ? i.getFechaInscripcion().toString() : null;
        dto.activa              = i.isActiva();
        return dto;
    }

    public Long getId() { return id; }
    public Long getSeccionId() { return seccionId; }
    public String getEstudianteUsername() { return estudianteUsername; }
    public String getFechaInscripcion() { return fechaInscripcion; }
    public boolean isActiva() { return activa; }
}
