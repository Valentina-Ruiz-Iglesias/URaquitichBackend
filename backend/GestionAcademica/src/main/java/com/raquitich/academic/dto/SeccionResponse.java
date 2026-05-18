package com.raquitich.academic.dto;

import com.raquitich.academic.model.Seccion;

import java.util.List;

public class SeccionResponse {

    private Long id;
    private Long asignaturaId;
    private String asignaturaCodigo;
    private String asignaturaNombre;
    private String periodo;
    private Integer cupoMaximo;
    private String docenteUsername;
    private boolean activa;
    private int cantidadInscritos;
    private List<HorarioResponse> horarios;

    public static SeccionResponse from(Seccion s) {
        SeccionResponse dto = new SeccionResponse();
        dto.id               = s.getId();
        dto.asignaturaId     = s.getAsignatura().getId();
        dto.asignaturaCodigo = s.getAsignatura().getCodigo();
        dto.asignaturaNombre = s.getAsignatura().getNombre();
        dto.periodo          = s.getPeriodo();
        dto.cupoMaximo       = s.getCupoMaximo();
        dto.docenteUsername  = s.getDocenteUsername();
        dto.activa           = s.isActiva();
        dto.cantidadInscritos = (int) s.getInscripciones().stream().filter(i -> i.isActiva()).count();
        dto.horarios         = s.getHorarios().stream().map(HorarioResponse::from).toList();
        return dto;
    }

    public Long getId() { return id; }
    public Long getAsignaturaId() { return asignaturaId; }
    public String getAsignaturaCodigo() { return asignaturaCodigo; }
    public String getAsignaturaNombre() { return asignaturaNombre; }
    public String getPeriodo() { return periodo; }
    public Integer getCupoMaximo() { return cupoMaximo; }
    public String getDocenteUsername() { return docenteUsername; }
    public boolean isActiva() { return activa; }
    public int getCantidadInscritos() { return cantidadInscritos; }
    public List<HorarioResponse> getHorarios() { return horarios; }
}
