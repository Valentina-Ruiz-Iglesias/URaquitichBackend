package com.raquitich.observaciones.service;

import com.raquitich.observaciones.dto.ObservacionRequest;
import com.raquitich.observaciones.dto.ObservacionResponse;
import com.raquitich.observaciones.model.Observacion;
import com.raquitich.observaciones.repository.ObservacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ObservacionService {

    private final ObservacionRepository repository;

    public ObservacionService(ObservacionRepository repository) {
        this.repository = repository;
    }

    public List<ObservacionResponse> listarTodas() {
        return repository.findAllByOrderByCreadoEnDesc()
                .stream()
                .map(ObservacionResponse::from)
                .toList();
    }

    public List<ObservacionResponse> listarPorEstudiante(String username) {
        return repository.findByEstudianteUsername(username)
                .stream()
                .map(ObservacionResponse::from)
                .toList();
    }

    @Transactional
    public ObservacionResponse registrar(ObservacionRequest request, String docenteUsername) {
        if (request.getEstudianteUsername() == null || request.getEstudianteUsername().isBlank()) {
            throw new IllegalArgumentException("El username del estudiante es obligatorio");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de observación es obligatorio");
        }
        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (request.getContenido() == null || request.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido es obligatorio");
        }

        Observacion obs = new Observacion();
        obs.setEstudianteUsername(request.getEstudianteUsername().trim());
        obs.setDocenteUsername(docenteUsername);
        obs.setTipo(request.getTipo().toUpperCase());
        obs.setTitulo(request.getTitulo());
        obs.setContenido(request.getContenido());

        // Parsear fecha solo si viene informada y no está vacía
        if (request.getFechaObservacion() != null && !request.getFechaObservacion().isBlank()) {
            try {
                obs.setFechaObservacion(LocalDate.parse(request.getFechaObservacion()));
            } catch (Exception e) {
                // Fecha inválida → se guarda sin fecha
            }
        }

        return ObservacionResponse.from(repository.save(obs));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Observación no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }
}
