package com.raquitich.academic.controller;

import com.raquitich.academic.dto.*;
import com.raquitich.academic.service.SeccionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secciones")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class SeccionController implements BaseController<SeccionRequest, SeccionRequest, SeccionResponse, Long> {

    private final SeccionService service;

    public SeccionController(SeccionService service) {
        this.service = service;
    }

    // ── Secciones (Métodos del BaseController) ─────────────────────────────────

    @Override
    @GetMapping
    public ResponseEntity<List<SeccionResponse>> getAll() {
        return ResponseEntity.ok(service.listar());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SeccionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<SeccionResponse> save(@Valid @RequestBody SeccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<SeccionResponse> update(
            @PathVariable Long id,
            @RequestBody SeccionRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Métodos Específicos (No están en el BaseController) ───────────────────

    /** Secciones en las que el estudiante autenticado está inscrito */
    @GetMapping("/mis-secciones")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<List<SeccionResponse>> misSecciones(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.seccionesDelEstudiante(username));
    }

    /** Secciones que el docente autenticado tiene asignadas */
    @GetMapping("/mis-secciones-docente")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<List<SeccionResponse>> misSeccionesDocente(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.seccionesDelDocente(username));
    }

    // ── Horarios ───────────────────────────────────────────────────────────────

    @PostMapping("/{seccionId}/horarios")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<HorarioResponse> agregarHorario(
            @PathVariable Long seccionId,
            @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agregarHorario(seccionId, request));
    }

    @DeleteMapping("/{seccionId}/horarios/{horarioId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<Void> eliminarHorario(
            @PathVariable Long seccionId,
            @PathVariable Long horarioId) {
        service.eliminarHorario(seccionId, horarioId);
        return ResponseEntity.noContent().build();
    }

    // ── Inscripciones ──────────────────────────────────────────────────────────

    @GetMapping("/{seccionId}/inscripciones")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<List<InscripcionResponse>> listarInscritos(@PathVariable Long seccionId) {
        return ResponseEntity.ok(service.listarInscritos(seccionId));
    }

    @PostMapping("/{seccionId}/inscripciones")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<InscripcionResponse> inscribir(
            @PathVariable Long seccionId,
            @Valid @RequestBody InscripcionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.inscribir(seccionId, request));
    }

    @DeleteMapping("/{seccionId}/inscripciones/{inscripcionId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO')")
    public ResponseEntity<Void> eliminarInscripcion(
            @PathVariable Long seccionId,
            @PathVariable Long inscripcionId) {
        service.eliminarInscripcion(seccionId, inscripcionId);
        return ResponseEntity.noContent().build();
    }
}