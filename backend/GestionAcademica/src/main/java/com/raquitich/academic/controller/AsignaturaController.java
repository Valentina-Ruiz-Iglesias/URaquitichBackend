package com.raquitich.academic.controller;

import com.raquitich.academic.dto.AsignaturaRequest;
import com.raquitich.academic.dto.AsignaturaResponse;
import com.raquitich.academic.service.AsignaturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaturas")
public class AsignaturaController {

    private final AsignaturaService service;

    public AsignaturaController(AsignaturaService service) {
        this.service = service;
    }

    /** Todos los roles autenticados pueden ver asignaturas */
    @GetMapping
    public ResponseEntity<List<AsignaturaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    /** Solo DIRECTIVO puede crear, actualizar o desactivar */
    @PostMapping
    @PreAuthorize("hasRole('DIRECTIVO')")
    public ResponseEntity<AsignaturaResponse> crear(@Valid @RequestBody AsignaturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTIVO')")
    public ResponseEntity<AsignaturaResponse> actualizar(
            @PathVariable Long id,
            @RequestBody AsignaturaRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTIVO')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
