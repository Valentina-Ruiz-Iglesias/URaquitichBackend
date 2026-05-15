package com.raquitich.observaciones.controller;

import com.raquitich.observaciones.dto.ObservacionRequest;
import com.raquitich.observaciones.dto.ObservacionResponse;
import com.raquitich.observaciones.model.TipoObservacion;
import com.raquitich.observaciones.service.ObservacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observaciones")
public class ObservacionController {

    private final ObservacionService service;

    public ObservacionController(ObservacionService service) {
        this.service = service;
    }

    /** Docentes y directivos registran una observación. El autor viene del JWT. */
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<ObservacionResponse> registrar(
            @Valid @RequestBody ObservacionRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(request, authentication.getName()));
    }

    /** Docentes y directivos editan una observación. */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<ObservacionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ObservacionRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    /** Directivos y docentes ven todas las observaciones. */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<List<ObservacionResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    /** Ver observaciones de un estudiante específico (con filtro de tipo opcional). */
    @GetMapping("/estudiante/{username}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<List<ObservacionResponse>> listarPorEstudiante(
            @PathVariable String username,
            @RequestParam(required = false) TipoObservacion tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(service.listarPorEstudianteYTipo(username, tipo));
        }
        return ResponseEntity.ok(service.listarPorEstudiante(username));
    }

    /** El estudiante ve SOLO sus propias observaciones. */
    @GetMapping("/mis-observaciones")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ObservacionResponse>> misObservaciones(Authentication authentication) {
        return ResponseEntity.ok(service.listarPorEstudiante(authentication.getName()));
    }

    /** El docente/directivo ve las observaciones que él mismo creó. */
    @GetMapping("/mis-registros")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<List<ObservacionResponse>> misRegistros(Authentication authentication) {
        return ResponseEntity.ok(service.listarPorAutor(authentication.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<ObservacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
