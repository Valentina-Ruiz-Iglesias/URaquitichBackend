package com.raquitich.observaciones.dto;

import com.raquitich.observaciones.model.TipoObservacion;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ObservacionRequest {

    @NotBlank(message = "El username del estudiante es obligatorio")
    @Size(max = 50)
    private String estudianteUsername;

    @NotNull(message = "El tipo de observación es obligatorio")
    private TipoObservacion tipo;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    private LocalDate fechaObservacion;

    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String v) { this.estudianteUsername = v; }

    public TipoObservacion getTipo() { return tipo; }
    public void setTipo(TipoObservacion v) { this.tipo = v; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String v) { this.titulo = v; }

    public String getContenido() { return contenido; }
    public void setContenido(String v) { this.contenido = v; }

    public LocalDate getFechaObservacion() { return fechaObservacion; }
    public void setFechaObservacion(LocalDate v) { this.fechaObservacion = v; }
}
