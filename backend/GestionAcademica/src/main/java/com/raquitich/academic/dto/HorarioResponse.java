package com.raquitich.academic.dto;

import com.raquitich.academic.model.Horario;

public class HorarioResponse {

    private Long id;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private String sala;

    public static HorarioResponse from(Horario h) {
        HorarioResponse dto = new HorarioResponse();
        dto.id         = h.getId();
        dto.diaSemana  = h.getDiaSemana();
        dto.horaInicio = h.getHoraInicio();
        dto.horaFin    = h.getHoraFin();
        dto.sala       = h.getSala();
        return dto;
    }

    public Long getId() { return id; }
    public String getDiaSemana() { return diaSemana; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getSala() { return sala; }
}
