package com.raquitich.observaciones.service;

import com.raquitich.observaciones.dto.ObservacionRequest;
import com.raquitich.observaciones.dto.ObservacionResponse;
import com.raquitich.observaciones.model.Observacion;
import com.raquitich.observaciones.model.TipoObservacion;
import com.raquitich.observaciones.repository.ObservacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObservacionService {

    private final ObservacionRepository repo;

    public ObservacionService(ObservacionRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ObservacionResponse registrar(ObservacionRequest request, String autorUsername) {
        Observacion o = new Observacion();
        o.setEstudianteUsername(request.getEstudianteUsername());
        o.setAutorUsername(autorUsername);
        o.setTipo(request.getTipo());
        o.setTitulo(request.getTitulo());
        o.setContenido(request.getContenido());
        o.setFechaObservacion(request.getFechaObservacion());
        return new ObservacionResponse(repo.save(o));
    }

    @Transactional
    public ObservacionResponse actualizar(Long id, ObservacionRequest request) {
        Observacion o = buscar(id);
        o.setEstudianteUsername(request.getEstudianteUsername());
        o.setTipo(request.getTipo());
        o.setTitulo(request.getTitulo());
        o.setContenido(request.getContenido());
        if (request.getFechaObservacion() != null) {
            o.setFechaObservacion(request.getFechaObservacion());
        }
        return new ObservacionResponse(repo.save(o));
    }

    public List<ObservacionResponse> listarTodas() {
        return repo.findAllByOrderByFechaObservacionDesc()
                .stream().map(ObservacionResponse::new).toList();
    }

    public List<ObservacionResponse> listarPorEstudiante(String username) {
        return repo.findByEstudianteUsernameOrderByFechaObservacionDesc(username)
                .stream().map(ObservacionResponse::new).toList();
    }

    public List<ObservacionResponse> listarPorEstudianteYTipo(String username, TipoObservacion tipo) {
        return repo.findByEstudianteUsernameAndTipoOrderByFechaObservacionDesc(username, tipo)
                .stream().map(ObservacionResponse::new).toList();
    }

    public List<ObservacionResponse> listarPorAutor(String autorUsername) {
        return repo.findByAutorUsernameOrderByFechaObservacionDesc(autorUsername)
                .stream().map(ObservacionResponse::new).toList();
    }

    public ObservacionResponse obtenerPorId(Long id) {
        return new ObservacionResponse(buscar(id));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Observación no encontrada");
        }
        repo.deleteById(id);
    }

    private Observacion buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Observación no encontrada con id: " + id));
    }
}
