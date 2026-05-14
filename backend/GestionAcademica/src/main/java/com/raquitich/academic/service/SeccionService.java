package com.raquitich.academic.service;

import com.raquitich.academic.dto.*;
import com.raquitich.academic.model.*;
import com.raquitich.academic.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeccionService {

    private final SeccionRepository     seccionRepo;
    private final AsignaturaRepository  asignaturaRepo;
    private final HorarioRepository     horarioRepo;
    private final InscripcionRepository inscripcionRepo;

    public SeccionService(SeccionRepository seccionRepo,
                          AsignaturaRepository asignaturaRepo,
                          HorarioRepository horarioRepo,
                          InscripcionRepository inscripcionRepo) {
        this.seccionRepo     = seccionRepo;
        this.asignaturaRepo  = asignaturaRepo;
        this.horarioRepo     = horarioRepo;
        this.inscripcionRepo = inscripcionRepo;
    }

    // ── Secciones ──────────────────────────────────────────────────────────────

    public List<SeccionResponse> listar() {
        return seccionRepo.findAll().stream().map(SeccionResponse::from).toList();
    }

    public SeccionResponse obtener(Long id) {
        return SeccionResponse.from(buscarSeccion(id));
    }

    public SeccionResponse crear(SeccionRequest request) {
        Asignatura asignatura = asignaturaRepo.findById(request.getAsignaturaId())
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        Seccion s = new Seccion();
        s.setAsignatura(asignatura);
        s.setPeriodo(request.getPeriodo());
        s.setCupoMaximo(request.getCupoMaximo());
        s.setDocenteUsername(request.getDocenteUsername());
        return SeccionResponse.from(seccionRepo.save(s));
    }

    public SeccionResponse actualizar(Long id, SeccionRequest request) {
        Seccion s = buscarSeccion(id);

        Asignatura asignatura = asignaturaRepo.findById(request.getAsignaturaId())
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        s.setAsignatura(asignatura);
        s.setPeriodo(request.getPeriodo());
        s.setCupoMaximo(request.getCupoMaximo());
        s.setDocenteUsername(request.getDocenteUsername());
        return SeccionResponse.from(seccionRepo.save(s));
    }

    public void eliminar(Long id) {
        Seccion s = buscarSeccion(id);
        s.setActiva(false);
        seccionRepo.save(s);
    }

    // ── Horarios ───────────────────────────────────────────────────────────────

    public List<HorarioResponse> listarHorarios(Long seccionId) {
        return horarioRepo.findBySeccionId(seccionId).stream()
                .map(HorarioResponse::from).toList();
    }

    public HorarioResponse agregarHorario(Long seccionId, HorarioRequest request) {
        Seccion s = buscarSeccion(seccionId);

        Horario h = new Horario();
        h.setSeccion(s);
        h.setDiaSemana(request.getDiaSemana());
        h.setHoraInicio(request.getHoraInicio());
        h.setHoraFin(request.getHoraFin());
        h.setSala(request.getSala());
        return HorarioResponse.from(horarioRepo.save(h));
    }

    public void eliminarHorario(Long seccionId, Long horarioId) {
        Horario h = horarioRepo.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        if (!h.getSeccion().getId().equals(seccionId)) {
            throw new RuntimeException("El horario no pertenece a la sección indicada");
        }
        horarioRepo.delete(h);
    }

    // ── Inscripciones ──────────────────────────────────────────────────────────

    public List<InscripcionResponse> listarInscritos(Long seccionId) {
        return inscripcionRepo.findBySeccionId(seccionId).stream()
                .map(InscripcionResponse::from).toList();
    }

    public InscripcionResponse inscribir(Long seccionId, InscripcionRequest request) {
        Seccion s = buscarSeccion(seccionId);

        if (inscripcionRepo.existsBySeccionIdAndEstudianteUsername(seccionId, request.getEstudianteUsername())) {
            throw new RuntimeException("El estudiante ya está inscrito en esta sección");
        }

        long actuales = s.getInscripciones().stream().filter(Inscripcion::isActiva).count();
        if (actuales >= s.getCupoMaximo()) {
            throw new RuntimeException("La sección ya alcanzó el cupo máximo");
        }

        Inscripcion i = new Inscripcion();
        i.setSeccion(s);
        i.setEstudianteUsername(request.getEstudianteUsername());
        return InscripcionResponse.from(inscripcionRepo.save(i));
    }

    public void eliminarInscripcion(Long seccionId, Long inscripcionId) {
        Inscripcion i = inscripcionRepo.findById(inscripcionId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        if (!i.getSeccion().getId().equals(seccionId)) {
            throw new RuntimeException("La inscripción no pertenece a la sección indicada");
        }
        i.setActiva(false);
        inscripcionRepo.save(i);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Seccion buscarSeccion(Long id) {
        return seccionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada con id: " + id));
    }
}
