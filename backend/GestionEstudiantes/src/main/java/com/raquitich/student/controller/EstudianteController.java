package com.raquitich.student.controller;

import com.raquitich.student.dto.EstudianteRequest;
import com.raquitich.student.dto.EstudianteResponse;
import com.raquitich.student.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    /**
     * Solo directivos y admins pueden crear estudiantes.
     * El directivo envía el JWT obtenido en el login.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody EstudianteRequest request) {
        EstudianteResponse response = estudianteService.crearEstudiante(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN', 'DOCENTE')")
    public ResponseEntity<List<EstudianteResponse>> listar() {
        return ResponseEntity.ok(estudianteService.listarEstudiantes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN', 'DOCENTE')")
    public ResponseEntity<EstudianteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EstudianteResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.desactivarEstudiante(id));
    }
}
