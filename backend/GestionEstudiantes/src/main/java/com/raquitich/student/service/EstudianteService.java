package com.raquitich.student.service;

import com.raquitich.student.client.AuthServiceClient;
import com.raquitich.student.dto.EstudianteRequest;
import com.raquitich.student.dto.EstudianteResponse;
import com.raquitich.student.model.Estudiante;
import com.raquitich.student.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final AuthServiceClient authServiceClient;

    public EstudianteService(EstudianteRepository estudianteRepository,
                             AuthServiceClient authServiceClient) {
        this.estudianteRepository = estudianteRepository;
        this.authServiceClient = authServiceClient;
    }

    /**
     * Crea un estudiante en db_estudiantes Y registra sus credenciales
     * en ServicioAutenticacion para que pueda hacer login.
     *
     * Flujo:
     *  1. Validar unicidad en esta BD
     *  2. Llamar a ServicioAutenticacion para crear las credenciales
     *  3. Guardar el estudiante en db_estudiantes
     *
     * Si ServicioAutenticacion falla, la transacción se revierte
     * y el estudiante NO queda guardado.
     */
    @Transactional
    public EstudianteResponse crearEstudiante(EstudianteRequest request) {
        if (estudianteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un estudiante con ese email");
        }
        if (estudianteRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Ya existe un estudiante con ese username");
        }
        if (request.getNumeroMatricula() != null
                && estudianteRepository.existsByNumeroMatricula(request.getNumeroMatricula())) {
            throw new RuntimeException("Ya existe un estudiante con ese número de matrícula");
        }

        // Crear credenciales en ServicioAutenticacion ANTES de guardar el estudiante.
        // Si falla, se lanza RuntimeException y no se persiste nada.
        authServiceClient.crearCredenciales(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getNombre(),
                "ROLE_ESTUDIANTE"
        );

        Estudiante estudiante = new Estudiante();
        estudiante.setUsername(request.getUsername());
        estudiante.setEmail(request.getEmail());
        estudiante.setNombre(request.getNombre());
        estudiante.setFechaNacimiento(request.getFechaNacimiento());
        estudiante.setNumeroMatricula(request.getNumeroMatricula());
        estudiante.setCarrera(request.getCarrera());
        estudiante.setAnioIngreso(request.getAnioIngreso());
        estudiante.setActivo(true);

        Estudiante guardado = estudianteRepository.save(estudiante);
        return new EstudianteResponse(guardado);
    }

    public List<EstudianteResponse> listarEstudiantes() {
        return estudianteRepository.findByActivoTrue()
                .stream()
                .map(EstudianteResponse::new)
                .toList();
    }

    public EstudianteResponse obtenerPorId(Long id) {
        Estudiante e = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return new EstudianteResponse(e);
    }

    @Transactional
    public EstudianteResponse desactivarEstudiante(Long id) {
        Estudiante e = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        e.setActivo(false);
        return new EstudianteResponse(estudianteRepository.save(e));
    }
}
