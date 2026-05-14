package com.raquitich.academic.repository;

import com.raquitich.academic.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findBySeccionId(Long seccionId);
    List<Inscripcion> findByEstudianteUsername(String username);
    boolean existsBySeccionIdAndEstudianteUsername(Long seccionId, String username);
    Optional<Inscripcion> findBySeccionIdAndEstudianteUsername(Long seccionId, String username);
}
