package com.raquitich.observaciones.dto;

public class ObservacionRequest {

    private String estudianteUsername;
    private String tipo;
    private String titulo;
    private String contenido;
    /** Fecha opcional en formato YYYY-MM-DD (o null) */
    private String fechaObservacion;

    public String getEstudianteUsername() { return estudianteUsername; }
    public void setEstudianteUsername(String estudianteUsername) { this.estudianteUsername = estudianteUsername; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getFechaObservacion() { return fechaObservacion; }
    public void setFechaObservacion(String fechaObservacion) { this.fechaObservacion = fechaObservacion; }
}
