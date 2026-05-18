package com.raquitich.talleres.exceptions;

public class TallerNotFoundException extends RuntimeException {

    public TallerNotFoundException(Long id) {
        super("No se encontró el taller con ID: " + id);
    }
}
