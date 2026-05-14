package com.raquitich.academic.dto;

import com.raquitich.academic.model.Asignatura;

public class AsignaturaResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private boolean activa;

    public AsignaturaResponse() {}

    public static AsignaturaResponse from(Asignatura a) {
        AsignaturaResponse r = new AsignaturaResponse();
        r.id          = a.getId();
        r.codigo      = a.getCodigo();
        r.nombre      = a.getNombre();
        r.descripcion = a.getDescripcion();
        r.creditos    = a.getCreditos();
        r.activa      = a.isActiva();
        return r;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Integer getCreditos() { return creditos; }
    public boolean isActiva() { return activa; }
}
