package com.raquitich.talleres.controllers;

import com.raquitich.talleres.dtos.TallerRequestDTO;
import com.raquitich.talleres.dtos.TallerResponseDTO;
import com.raquitich.talleres.services.TallerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talleres")
@RequiredArgsConstructor
public class TallerController implements BaseController<TallerRequestDTO, TallerResponseDTO, Long> {

    private final TallerService tallerService;

    @Override
    @GetMapping
    public ResponseEntity<List<TallerResponseDTO>> getAll() {
        return ResponseEntity.ok(tallerService.listarTalleres());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.obtenerTallerPorId(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<TallerResponseDTO> save(@Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.crearTaller(request));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.ok(tallerService.actualizarTaller(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tallerService.eliminarTaller(id);
        return ResponseEntity.noContent().build();
    }

    // Este método se queda igual y SIN @Override porque es una acción específica 
    // de este microservicio, no pertenece al CRUD genérico del BaseController.
    @PostMapping("/{id}/inscribir")
    public ResponseEntity<TallerResponseDTO> inscribirEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.inscribirEstudiante(id));
    }
}