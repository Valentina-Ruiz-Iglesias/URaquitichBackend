package com.raquitich.student.service;

import com.raquitich.student.client.AuthServiceClient;
import com.raquitich.student.dto.EstudianteRequest;
import com.raquitich.student.dto.EstudianteResponse;
import com.raquitich.student.dto.EstudianteUpdateRequest;
import com.raquitich.student.model.Estudiante;
import com.raquitich.student.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio — aquí vive toda la LÓGICA DE NEGOCIO.
 * El controller solo recibe la petición HTTP y se la pasa al servicio.
 * El servicio decide qué hacer: validar, llamar al repositorio, etc.
 */
@Service
public class EstudianteService {

    // Repositorio para hablar con la base de datos de estudiantes (db_estudiantes)
    private final EstudianteRepository estudianteRepository;

    // Cliente HTTP que habla con ServicioAutenticacion (puerto 8081)
    private final AuthServiceClient authServiceClient;

    // Spring inyecta los dos automáticamente — no hace falta "new"
    public EstudianteService(EstudianteRepository estudianteRepository,
                             AuthServiceClient authServiceClient) {
        this.estudianteRepository = estudianteRepository;
        this.authServiceClient = authServiceClient;
    }

    /**
     * CREAR estudiante — flujo de dos pasos:
     *
     *  Paso 1: Crear las credenciales en ServicioAutenticacion (username + password)
     *  Paso 2: Guardar los datos de perfil en db_estudiantes
     *
     * Si el paso 1 falla, @Transactional revierte automáticamente el paso 2.
     * Así nunca queda un estudiante en la BD sin poder hacer login.
     */
    @Transactional
    public EstudianteResponse crearEstudiante(EstudianteRequest request) {
        // Validar que no existan duplicados ANTES de hacer nada
        if (estudianteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un estudiante con ese email");
        }
        if (estudianteRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Ya existe un estudiante con ese username");
        }
        if (request.getRut() != null && estudianteRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("Ya existe un estudiante con ese RUT");
        }
        if (request.getNumeroMatricula() != null
                && estudianteRepository.existsByNumeroMatricula(request.getNumeroMatricula())) {
            throw new RuntimeException("Ya existe un estudiante con ese número de matrícula");
        }

        // Crear credenciales en ServicioAutenticacion
        // Si falla (ej: username duplicado allá), lanza excepción y no guardamos nada
        authServiceClient.crearCredenciales(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getNombre(),
                "ROLE_ESTUDIANTE"
        );

        // Construir la entidad y guardarla en db_estudiantes
        Estudiante estudiante = new Estudiante();
        estudiante.setUsername(request.getUsername());
        estudiante.setRut(request.getRut());
        estudiante.setEmail(request.getEmail());
        estudiante.setNombre(request.getNombre());
        estudiante.setFechaNacimiento(request.getFechaNacimiento());
        estudiante.setNumeroMatricula(request.getNumeroMatricula());
        estudiante.setCarrera(request.getCarrera());
        estudiante.setAnioIngreso(request.getAnioIngreso());
        estudiante.setActivo(true);

        Estudiante guardado = estudianteRepository.save(estudiante);

        // Convertir la entidad a DTO de respuesta (sin password)
        return new EstudianteResponse(guardado);
    }

    /**
     * LISTAR todos los estudiantes activos.
     * Solo los que tienen activo = true (los desactivados no aparecen).
     */
    public List<EstudianteResponse> listarEstudiantes() {
        return estudianteRepository.findByActivoTrue()
                .stream()
                .map(EstudianteResponse::new)  // convierte cada Estudiante → EstudianteResponse
                .toList();
    }

    /**
     * OBTENER un estudiante por su ID numérico.
     * Lanza excepción si no existe — el GlobalExceptionHandler la convierte en HTTP 409.
     */
    public EstudianteResponse obtenerPorId(Long id) {
        Estudiante e = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return new EstudianteResponse(e);
    }

    /**
     * OBTENER el perfil del estudiante que está logueado.
     * El username viene del token JWT (lo extrae el controller desde el SecurityContext).
     * Esto lo usa el rol ESTUDIANTE para ver sus propios datos.
     */
    public EstudianteResponse obtenerPorUsername(String username) {
        Estudiante e = estudianteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        return new EstudianteResponse(e);
    }

    /**
     * ACTUALIZAR datos de perfil de un estudiante existente.
     * Solo se actualizan los campos de perfil (nombre, carrera, etc.).
     * Username, email y password NO se tocan aquí — viven en ServicioAutenticacion.
     */
    @Transactional
    public EstudianteResponse actualizarEstudiante(Long id, EstudianteUpdateRequest request) {
        Estudiante e = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        if (request.getRut() != null
                && !request.getRut().equals(e.getRut())
                && estudianteRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("Ya existe un estudiante con ese RUT");
        }
        if (request.getNumeroMatricula() != null
                && !request.getNumeroMatricula().equals(e.getNumeroMatricula())
                && estudianteRepository.existsByNumeroMatricula(request.getNumeroMatricula())) {
            throw new RuntimeException("Ya existe un estudiante con ese número de matrícula");
        }

        e.setNombre(request.getNombre());
        e.setRut(request.getRut());
        e.setFechaNacimiento(request.getFechaNacimiento());
        e.setNumeroMatricula(request.getNumeroMatricula());
        e.setCarrera(request.getCarrera());
        e.setAnioIngreso(request.getAnioIngreso());

        return new EstudianteResponse(estudianteRepository.save(e));
    }

    /**
     * DESACTIVAR (borrado suave) — marca activo = false.
     * No elimina el registro de la BD para mantener historial.
     * El estudiante desaparecerá de la lista pero sus datos quedan guardados.
     */
    @Transactional
    public EstudianteResponse desactivarEstudiante(Long id) {
        Estudiante e = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        e.setActivo(false);
        return new EstudianteResponse(estudianteRepository.save(e));
    }
}
