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

    public SeccionResponse() {}

    public static SeccionResponse from(Seccion s) {
        SeccionResponse r = new SeccionResponse();
        r.id                = s.getId();
        r.asignaturaId      = s.getAsignatura().getId();
        r.asignaturaCodigo  = s.getAsignatura().getCodigo();
        r.asignaturaNombre  = s.getAsignatura().getNombre();
        r.periodo           = s.getPeriodo();
        r.cupoMaximo        = s.getCupoMaximo();
        r.docenteUsername   = s.getDocenteUsername();
        r.activa            = s.isActiva();
        r.cantidadInscritos = (int) s.getInscripciones().stream().filter(i -> i.isActiva()).count();
        r.horarios          = s.getHorarios().stream().map(HorarioResponse::from).toList();
        return r;
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
