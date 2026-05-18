package com.raquitich.academic.service;

import com.raquitich.academic.dto.AsignaturaRequest;
import com.raquitich.academic.dto.AsignaturaResponse;
import com.raquitich.academic.model.Asignatura;
import com.raquitich.academic.repository.AsignaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AsignaturaService {

    private final AsignaturaRepository repo;

    public AsignaturaService(AsignaturaRepository repo) {
        this.repo = repo;
    }

    public List<AsignaturaResponse> listar() {
        return repo.findAll().stream().map(AsignaturaResponse::from).toList();
    }

    public AsignaturaResponse obtener(Long id) {
        return repo.findById(id)
                .map(AsignaturaResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada"));
    }

    @Transactional
    public AsignaturaResponse crear(AsignaturaRequest request) {
        if (repo.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una asignatura con el código: " + request.getCodigo());
        }
        Asignatura a = new Asignatura();
        a.setCodigo(request.getCodigo().toUpperCase().trim());
        a.setNombre(request.getNombre().trim());
        a.setDescripcion(request.getDescripcion());
        a.setCreditos(request.getCreditos());
        return AsignaturaResponse.from(repo.save(a));
    }

    @Transactional
    public AsignaturaResponse actualizar(Long id, AsignaturaRequest request) {
        Asignatura a = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada"));
        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            a.setNombre(request.getNombre().trim());
        }
        if (request.getDescripcion() != null) {
            a.setDescripcion(request.getDescripcion());
        }
        if (request.getCreditos() != null) {
            a.setCreditos(request.getCreditos());
        }
        return AsignaturaResponse.from(repo.save(a));
    }

    @Transactional
    public void desactivar(Long id) {
        Asignatura a = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada"));
        a.setActiva(false);
        repo.save(a);
    }
}
