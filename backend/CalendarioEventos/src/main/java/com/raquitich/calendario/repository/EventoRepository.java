package com.raquitich.calendario.repository;

import com.raquitich.calendario.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    /** Todos los eventos institucionales */
    List<Evento> findByTipo(String tipo);

    /** Eventos personales de un estudiante */
    List<Evento> findByEstudianteUsername(String username);

    /** Eventos públicos (INSTITUCIONAL o PUBLICO) + personales del usuario */
    @Query("SELECT e FROM Evento e WHERE e.tipo = 'INSTITUCIONAL' OR e.tipo = 'PUBLICO' OR e.estudianteUsername = :username OR e.creadoPor = :username")
    List<Evento> findVisiblesParaUsuario(@Param("username") String username);

    /** Eventos creados por un usuario específico */
    List<Evento> findByCreadoPor(String username);
}
