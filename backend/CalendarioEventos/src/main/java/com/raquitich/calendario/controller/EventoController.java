package com.raquitich.calendario.controller;

import com.raquitich.calendario.config.JwtService;
import com.raquitich.calendario.dto.EventoRequest;
import com.raquitich.calendario.dto.EventoResponse;
import com.raquitich.calendario.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class EventoController {

    private final EventoService service;
    private final JwtService jwtService;

    public EventoController(EventoService service, JwtService jwtService) {
        this.service    = service;
        this.jwtService = jwtService;
    }

    /** Lista todos los eventos visibles para el usuario autenticado */
    @GetMapping
    public ResponseEntity<List<EventoResponse>> listar(Authentication auth,
                                                       HttpServletRequest request) {
        String role = extractRole(request);
        return ResponseEntity.ok(service.listar(auth.getName(), role));
    }

    /** Solo directivos crean eventos institucionales (visibles a todos) */
    @PostMapping("/institucional")
    public ResponseEntity<EventoResponse> crearInstitucional(
            @Valid @RequestBody EventoRequest req,
            Authentication auth,
            HttpServletRequest request) {
        String role = extractRole(request);
        if (!"ROLE_DIRECTIVO".equals(role) && !"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearInstitucional(req, auth.getName()));
    }

    /** Docentes crean eventos públicos visibles para todos (avisos de sección) */
    @PostMapping("/publico")
    public ResponseEntity<EventoResponse> crearPublico(
            @Valid @RequestBody EventoRequest req,
            Authentication auth,
            HttpServletRequest request) {
        String role = extractRole(request);
        if (!"ROLE_DOCENTE".equals(role) && !"ROLE_DIRECTIVO".equals(role) && !"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.crearPublico(req, auth.getName()));
    }

    /** Cualquier usuario autenticado puede crear un evento personal */
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
