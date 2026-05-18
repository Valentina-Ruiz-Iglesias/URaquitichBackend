package com.raquitich.observaciones.controller;

import com.raquitich.observaciones.dto.ObservacionRequest;
import com.raquitich.observaciones.dto.ObservacionResponse;
import com.raquitich.observaciones.service.ObservacionService;
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

    // Docente y Directivo ven todas las observaciones
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<List<ObservacionResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Estudiante ve solo sus propias observaciones
    @GetMapping("/mis-observaciones")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ObservacionResponse>> misObservaciones(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(service.listarPorEstudiante(username));
    }

    // Docente y Directivo pueden registrar observaciones
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<ObservacionResponse> registrar(
            @RequestBody ObservacionRequest request,
            Authentication auth
    ) {
        String docenteUsername = auth.getName();
        ObservacionResponse response = service.registrar(request, docenteUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Solo Directivo y Admin pueden eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
