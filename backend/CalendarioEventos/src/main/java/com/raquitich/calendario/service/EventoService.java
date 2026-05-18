package com.raquitich.calendario.service;

import com.raquitich.calendario.dto.EventoRequest;
import com.raquitich.calendario.dto.EventoResponse;
import com.raquitich.calendario.model.Evento;
import com.raquitich.calendario.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repo;

    public EventoService(EventoRepository repo) {
        this.repo = repo;
    }

    /** Devuelve eventos visibles para el usuario según su rol */
    public List<EventoResponse> listar(String username, String role) {
        List<Evento> eventos;
        if ("ROLE_DIRECTIVO".equals(role) || "ROLE_ADMIN".equals(role)) {
            eventos = repo.findAll();
        } else {
            eventos = repo.findVisiblesParaUsuario(username);
        }
        return eventos.stream().map(EventoResponse::from).toList();
    }

    @Transactional
    public EventoResponse crearInstitucional(EventoRequest request, String creadoPor) {
        Evento e = buildEvento(request, "INSTITUCIONAL", creadoPor);
        return EventoResponse.from(repo.save(e));
    }

    /** Docente publica un evento visible para todos (ej: aviso de su sección) */
    @Transactional
    public EventoResponse crearPublico(EventoRequest request, String creadoPor) {
        Evento e = buildEvento(request, "PUBLICO", creadoPor);
        return EventoResponse.from(repo.save(e));
    }

    @Transactional
    public EventoResponse crearPersonal(EventoRequest request, String creadoPor) {
        Evento e = buildEvento(request, "PERSONAL", creadoPor);
        e.setEstudianteUsername(creadoPor);
        return EventoResponse.from(repo.save(e));
    }

    @Transactional
    public EventoResponse actualizar(Long id, EventoRequest request, String username, String role) {
        Evento e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        validarPropietario(e, username, role);

        if (request.getTitulo() != null && !request.getTitulo().isBlank())
            e.setTitulo(request.getTitulo().trim());
        if (request.getDescripcion() != null)
            e.setDescripcion(request.getDescripcion());
        if (request.getFechaInicio() != null && !request.getFechaInicio().isBlank())
            e.setFechaInicio(LocalDate.parse(request.getFechaInicio()));
        if (request.getFechaFin() != null && !request.getFechaFin().isBlank())
            e.setFechaFin(LocalDate.parse(request.getFechaFin()));
        if (request.getHoraInicio() != null) e.setHoraInicio(request.getHoraInicio());
        if (request.getHoraFin() != null)    e.setHoraFin(request.getHoraFin());
        if (request.getColor() != null)      e.setColor(request.getColor());

        return EventoResponse.from(repo.save(e));
    }

    @Transactional
    public void eliminar(Long id, String username, String role) {
        Evento e = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        validarPropietario(e, username, role);
        repo.delete(e);
    }

    private void validarPropietario(Evento e, String username, String role) {
        boolean esDirectivo = "ROLE_DIRECTIVO".equals(role) || "ROLE_ADMIN".equals(role);
        if (!esDirectivo && !e.getCreadoPor().equals(username)) {
            throw new SecurityException("No tienes permiso para modificar este evento");
        }
    }

    private Evento buildEvento(EventoRequest req, String tipo, String creadoPor) {
        Evento e = new Evento();
        e.setTipo(tipo);
        e.setTitulo(req.getTitulo().trim());
        e.setDescripcion(req.getDescripcion());
        e.setFechaInicio(LocalDate.parse(req.getFechaInicio()));
        if (req.getFechaFin() != null && !req.getFechaFin().isBlank())
            e.setFechaFin(LocalDate.parse(req.getFechaFin()));
        e.setHoraInicio(req.getHoraInicio());
        e.setHoraFin(req.getHoraFin());
        e.setColor(req.getColor() != null ? req.getColor() : "#4f46e5");
        e.setCreadoPor(creadoPor);
        return e;
    }
}
