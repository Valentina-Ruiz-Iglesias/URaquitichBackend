package com.raquitich.talleres.exceptions;

public class CuposInsuficientesException extends RuntimeException {

    public CuposInsuficientesException(Long idTaller) {
        super("No hay cupos disponibles para el taller con ID: " + idTaller);
    }
}
