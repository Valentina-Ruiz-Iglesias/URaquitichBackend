package com.raquitich.academic.service;

import com.raquitich.academic.dto.*;
import com.raquitich.academic.model.*;
import com.raquitich.academic.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeccionService {

    private final SeccionRepository seccionRepo;
    private final AsignaturaRepository asignaturaRepo;
    private final HorarioRepository horarioRepo;
    private final InscripcionRepository inscripcionRepo;

    public SeccionService(SeccionRepository seccionRepo,
                          AsignaturaRepository asignaturaRepo,
                          HorarioRepository horarioRepo,
                          InscripcionRepository inscripcionRepo) {
        this.seccionRepo    = seccionRepo;
        this.asignaturaRepo = asignaturaRepo;
        this.horarioRepo    = horarioRepo;
        this.inscripcionRepo = inscripcionRepo;
    }

    // ── Secciones ──────────────────────────────────────────────────────────────

    public List<SeccionResponse> listar() {
        return seccionRepo.findAll().stream().map(SeccionResponse::from).toList();
    }

    /** Devuelve las secciones activas en las que el estudiante está inscrito */
    @Transactional(readOnly = true)
    public List<SeccionResponse> seccionesDelEstudiante(String username) {
        return inscripcionRepo.findByEstudianteUsername(username).stream()
                .filter(i -> i.isActiva())
                .map(i -> SeccionResponse.from(i.getSeccion()))
                .toList();
    }

    /** Devuelve las secciones activas que el docente tiene asignadas */
    @Transactional(readOnly = true)
    public List<SeccionResponse> seccionesDelDocente(String username) {
        return seccionRepo.findByDocenteUsername(username).stream()
                .filter(Seccion::isActiva)
                .map(SeccionResponse::from)
                .toList();
    }

    public SeccionResponse obtener(Long id) {
        return seccionRepo.findById(id)
                .map(SeccionResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));
    }

    @Transactional
    public SeccionResponse crear(SeccionRequest request) {
        Asignatura asignatura = asignaturaRepo.findById(request.getAsignaturaId())
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada con id: " + request.getAsignaturaId()));

        Seccion s = new Seccion();
        s.setAsignatura(asignatura);
        s.setPeriodo(request.getPeriodo().trim());
        s.setCupoMaximo(request.getCupoMaximo() != null ? request.getCupoMaximo() : 40);
        s.setDocenteUsername(request.getDocenteUsername().trim());
        return SeccionResponse.from(seccionRepo.save(s));
    }

    @Transactional
    public SeccionResponse actualizar(Long id, SeccionRequest request) {
        Seccion s = seccionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));
        if (request.getAsignaturaId() != null) {
            Asignatura a = asignaturaRepo.findById(request.getAsignaturaId())
                    .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada"));
            s.setAsignatura(a);
        }
        if (request.getPeriodo() != null && !request.getPeriodo().isBlank()) {
            s.setPeriodo(request.getPeriodo().trim());
        }
        if (request.getCupoMaximo() != null) {
            s.setCupoMaximo(request.getCupoMaximo());
        }
        if (request.getDocenteUsername() != null && !request.getDocenteUsername().isBlank()) {
            s.setDocenteUsername(request.getDocenteUsername().trim());
        }
        return SeccionResponse.from(seccionRepo.save(s));
    }

    @Transactional
    public void desactivar(Long id) {
        Seccion s = seccionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));
        s.setActiva(false);
        seccionRepo.save(s);
    }

    // ── Horarios ───────────────────────────────────────────────────────────────

    @Transactional
    public HorarioResponse agregarHorario(Long seccionId, HorarioRequest request) {
        Seccion s = seccionRepo.findById(seccionId)
                .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));
        Horario h = new Horario();
        h.setSeccion(s);
        h.setDiaSemana(request.getDiaSemana().toUpperCase().trim());
        h.setHoraInicio(request.getHoraInicio().trim());
        h.setHoraFin(request.getHoraFin().trim());
        h.setSala(request.getSala());
        return HorarioResponse.from(horarioRepo.save(h));
    }

    @Transactional
    public void eliminarHorario(Long seccionId, Long horarioId) {
        Horario h = horarioRepo.findById(horarioId)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));
        if (!h.getSeccion().getId().equals(seccionId)) {
            throw new IllegalArgumentException("El horario no pertenece a la sección indicada");
        }
        horarioRepo.delete(h);
    }

    // ── Inscripciones ──────────────────────────────────────────────────────────

    public List<InscripcionResponse> listarInscritos(Long seccionId) {
        return inscripcionRepo.findBySeccionId(seccionId).stream()
                .map(InscripcionResponse::from).toList();
    }

    @Transactional
    public InscripcionResponse inscribir(Long seccionId, InscripcionRequest request) {
        Seccion s = seccionRepo.findById(seccionId)
                .orElseThrow(() -> new IllegalArgumentException("Sección no encontrada"));

        if (inscripcionRepo.existsBySeccionIdAndEstudianteUsername(seccionId, request.getEstudianteUsername())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esta sección");
        }

        long inscritos = s.getInscripciones().stream().filter(Inscripcion::isActiva).count();
        if (inscritos >= s.getCupoMaximo()) {
            throw new IllegalArgumentException("La sección ya alcanzó el cupo máximo de " + s.getCupoMaximo() + " estudiantes");
        }

        Inscripcion i = new Inscripcion();
        i.setSeccion(s);
        i.setEstudianteUsername(request.getEstudianteUsername().trim());
        return InscripcionResponse.from(inscripcionRepo.save(i));
    }

    @Transactional
    public void eliminarInscripcion(Long seccionId, Long inscripcionId) {
        Inscripcion i = inscripcionRepo.findById(inscripcionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        if (!i.getSeccion().getId().equals(seccionId)) {
            throw new IllegalArgumentException("La inscripción no pertenece a la sección indicada");
        }
        inscripcionRepo.delete(i);
    }
}
