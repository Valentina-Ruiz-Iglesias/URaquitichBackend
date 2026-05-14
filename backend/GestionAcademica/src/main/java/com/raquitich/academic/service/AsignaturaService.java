package com.raquitich.academic.service;

import com.raquitich.academic.dto.AsignaturaRequest;
import com.raquitich.academic.dto.AsignaturaResponse;
import com.raquitich.academic.model.Asignatura;
import com.raquitich.academic.repository.AsignaturaRepository;
import org.springframework.stereotype.Service;

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
        return AsignaturaResponse.from(buscar(id));
    }

    public AsignaturaResponse crear(AsignaturaRequest request) {
        if (repo.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Ya existe una asignatura con el código: " + request.getCodigo());
        }
        Asignatura a = new Asignatura();
        a.setCodigo(request.getCodigo().toUpperCase());
        a.setNombre(request.getNombre());
        a.setDescripcion(request.getDescripcion());
        a.setCreditos(request.getCreditos());
        return AsignaturaResponse.from(repo.save(a));
    }

    public AsignaturaResponse actualizar(Long id, AsignaturaRequest request) {
        Asignatura a = buscar(id);

        // Si cambia el código, verificar que no exista
        if (!a.getCodigo().equals(request.getCodigo().toUpperCase()) &&
            repo.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Ya existe una asignatura con el código: " + request.getCodigo());
        }

        a.setCodigo(request.getCodigo().toUpperCase());
        a.setNombre(request.getNombre());
        a.setDescripcion(request.getDescripcion());
        a.setCreditos(request.getCreditos());
        return AsignaturaResponse.from(repo.save(a));
    }

    public void eliminar(Long id) {
        Asignatura a = buscar(id);
        a.setActiva(false);
        repo.save(a);
    }

    private Asignatura buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con id: " + id));
    }
}
