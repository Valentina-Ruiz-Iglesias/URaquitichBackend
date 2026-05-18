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
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class TallerController {

    private final TallerService tallerService;

    @GetMapping
    public ResponseEntity<List<TallerResponseDTO>> listarTalleres() {
        return ResponseEntity.ok(tallerService.listarTalleres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> obtenerTaller(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.obtenerTallerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TallerResponseDTO> crearTaller(@Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.crearTaller(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TallerResponseDTO> actualizarTaller(
            @PathVariable Long id,
            @Valid @RequestBody TallerRequestDTO request) {
        return ResponseEntity.ok(tallerService.actualizarTaller(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTaller(@PathVariable Long id) {
        tallerService.eliminarTaller(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/inscribir")
    public ResponseEntity<TallerResponseDTO> inscribirEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(tallerService.inscribirEstudiante(id));
    }
}
