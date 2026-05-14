package com.raquitich.academic.dto;

import com.raquitich.academic.model.DiaSemana;
import com.raquitich.academic.model.Horario;

public class HorarioResponse {

    private Long id;
    private DiaSemana diaSemana;
    private String horaInicio;
    private String horaFin;
    private String sala;

    public HorarioResponse() {}

    public static HorarioResponse from(Horario h) {
        HorarioResponse r = new HorarioResponse();
        r.id         = h.getId();
        r.diaSemana  = h.getDiaSemana();
        r.horaInicio = h.getHoraInicio();
        r.horaFin    = h.getHoraFin();
        r.sala       = h.getSala();
        return r;
    }

    public Long getId() { return id; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getSala() { return sala; }
}
