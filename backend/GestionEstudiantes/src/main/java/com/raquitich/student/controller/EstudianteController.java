package com.raquitich.student.controller;

import com.raquitich.student.dto.EstudianteRequest;
import com.raquitich.student.dto.EstudianteResponse;
import com.raquitich.student.dto.EstudianteUpdateRequest;
import com.raquitich.student.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller — punto de entrada HTTP del microservicio.
 * Recibe las peticiones del frontend y las delega al EstudianteService.
 *
 * @CrossOrigin permite que el frontend Angular (puerto 4200) llame a este backend
 * sin que el navegador bloquee la petición por CORS.
 */
@RestController
@RequestMapping("/estudiantes")
public class EstudianteController implements BaseController<EstudianteRequest, EstudianteUpdateRequest, EstudianteResponse, Long> {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    /**
     * POST /estudiantes
     * Crea un nuevo estudiante.
     * Solo pueden hacerlo DIRECTIVO y ADMIN.
     * Retorna HTTP 201 Created con los datos del estudiante creado.
     */
    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EstudianteResponse> save(@Valid @RequestBody EstudianteRequest request) {
        EstudianteResponse response = estudianteService.crearEstudiante(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /estudiantes
     * Lista todos los estudiantes activos.
     * Pueden verlo DIRECTIVO, ADMIN y DOCENTE (pero el docente no puede editar).
     */
    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN', 'DOCENTE')")
    public ResponseEntity<List<EstudianteResponse>> getAll() {
        return ResponseEntity.ok(estudianteService.listarEstudiantes());
    }

    /**
     * GET /estudiantes/mi-perfil
     * El estudiante logueado ve sus propios datos.
     * Authentication es inyectado por Spring Security — contiene el username del JWT.
     *
     * IMPORTANTE: este endpoint debe estar ANTES de /{id} en el orden,
     * porque si no, Spring intentaría interpretar "mi-perfil" como un ID numérico.
     */
    @GetMapping("/mi-perfil")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<EstudianteResponse> miPerfil(Authentication authentication) {
        // authentication.getName() devuelve el username que está dentro del JWT
        String username = authentication.getName();
        return ResponseEntity.ok(estudianteService.obtenerPorUsername(username));
    }

    /**
     * GET /estudiantes/{id}
     * Obtiene un estudiante específico por su ID.
     * Solo para DIRECTIVO, ADMIN y DOCENTE.
     */
    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN', 'DOCENTE')")
    public ResponseEntity<EstudianteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    /**
     * PUT /estudiantes/{id}
     * Actualiza los datos de perfil de un estudiante existente.
     * Solo DIRECTIVO y ADMIN pueden editar.
     * No cambia username, email ni contraseña (esos viven en ServicioAutenticacion).
     */
    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<EstudianteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteUpdateRequest request) {
        return ResponseEntity.ok(estudianteService.actualizarEstudiante(id, request));
    }

    /**
     * DELETE /estudiantes/{id}
     * Desactiva un estudiante (borrado suave — no elimina de la BD).
     * Solo DIRECTIVO y ADMIN pueden hacerlo.
     */
    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estudianteService.desactivarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}