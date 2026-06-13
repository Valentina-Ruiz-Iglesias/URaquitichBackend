package com.raquitich.talleres.controllers; // Revisa que esta ruta coincida con la tuya

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Molde genérico para todos los controladores del sistema.
 * @param <Req> El DTO de entrada (Request)
 * @param <Res> El DTO de salida (Response)
 * @param <ID> El tipo de dato del identificador (ej: Long, Integer)
 */
public interface BaseController<Req, Res, ID> {

    @GetMapping
    ResponseEntity<List<Res>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<Res> getById(@PathVariable ID id);

    @PostMapping
    ResponseEntity<Res> save(@RequestBody Req request);

    @PutMapping("/{id}")
    ResponseEntity<Res> update(@PathVariable ID id, @RequestBody Req request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable ID id);
}