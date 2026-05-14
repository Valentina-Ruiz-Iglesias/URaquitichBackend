package com.raquitich.academic.controller;

import com.raquitich.academic.dto.*;
import com.raquitich.academic.service.SeccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secciones")
public class SeccionController {

    private final SeccionService service;

    public SeccionController(SeccionService service) {
        this.service = service;
    }

    // ── Secciones ──────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<SeccionResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeccionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<SeccionResponse> crear(@Valid @RequestBody SeccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeccionResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody SeccionRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Horarios ───────────────────────────────────────────────────────────────

    @GetMapping("/{id}/horarios")
    public ResponseEntity<List<HorarioResponse>> listarHorarios(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarHorarios(id));
    }

    @PostMapping("/{id}/horarios")
    public ResponseEntity<HorarioResponse> agregarHorario(@PathVariable Long id,
                                                           @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agregarHorario(id, request));
    }

    @DeleteMapping("/{id}/horarios/{horarioId}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id,
                                                 @PathVariable Long horarioId) {
        service.eliminarHorario(id, horarioId);
        return ResponseEntity.noContent().build();
    }

    // ── Inscripciones ──────────────────────────────────────────────────────────

    @GetMapping("/{id}/inscripciones")
    public ResponseEntity<List<InscripcionResponse>> listarInscritos(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarInscritos(id));
    }

    @PostMapping("/{id}/inscripciones")
    public ResponseEntity<InscripcionResponse> inscribir(@PathVariable Long id,
                                                          @Valid @RequestBody InscripcionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.inscribir(id, request));
    }

    @DeleteMapping("/{id}/inscripciones/{inscripcionId}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id,
                                                     @PathVariable Long inscripcionId) {
        service.eliminarInscripcion(id, inscripcionId);
        return ResponseEntity.noContent().build();
    }
}
