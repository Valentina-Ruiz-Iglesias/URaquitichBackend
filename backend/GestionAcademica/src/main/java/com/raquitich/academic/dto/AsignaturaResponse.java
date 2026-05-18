package com.raquitich.academic.dto;

import com.raquitich.academic.model.Asignatura;

public class AsignaturaResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private boolean activa;

    public static AsignaturaResponse from(Asignatura a) {
        AsignaturaResponse dto = new AsignaturaResponse();
        dto.id          = a.getId();
        dto.codigo      = a.getCodigo();
        dto.nombre      = a.getNombre();
        dto.descripcion = a.getDescripcion();
        dto.creditos    = a.getCreditos();
        dto.activa      = a.isActiva();
        return dto;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Integer getCreditos() { return creditos; }
    public boolean isActiva() { return activa; }
}
