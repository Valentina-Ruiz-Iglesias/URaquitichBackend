package com.raquitich.calendario.controller;

import com.raquitich.calendario.config.JwtService;
import com.raquitich.calendario.dto.EventoRequest;
import com.raquitich.calendario.dto.EventoResponse;
import com.raquitich.calendario.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService service;
    private final JwtService jwtService;

    public EventoController(EventoService service, JwtService jwtService) {
        this.service    = service;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<EventoResponse>> listar(Authentication auth,
                                                       HttpServletRequest request) {
        String role = extractRole(request);
        return ResponseEntity.ok(service.listar(auth.getName(), role));
    }

    @PostMapping("/institucional")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EventoResponse> crearInstitucional(
            @Valid @RequestBody EventoRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearInstitucional(req, auth.getName()));
    }

    @PostMapping("/publico")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EventoResponse> crearPublico(
            @Valid @RequestBody EventoRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearPublico(req, auth.getName()));
    }

    @PostMapping("/personal")
    public ResponseEntity<EventoResponse> crearPersonal(
            @Valid @RequestBody EventoRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearPersonal(req, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody EventoRequest req,
            Authentication auth,
            HttpServletRequest request) {
        String role = extractRole(request);
        return ResponseEntity.ok(service.actualizar(id, req, auth.getName(), role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication auth,
            HttpServletRequest request) {
        String role = extractRole(request);
        service.eliminar(id, auth.getName(), role);
        return ResponseEntity.noContent().build();
    }

    private String extractRole(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return jwtService.extractRole(header.substring(7));
        }
        return "";
    }
}
