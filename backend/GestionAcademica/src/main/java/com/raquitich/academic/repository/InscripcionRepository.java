package com.raquitich.academic.repository;

import com.raquitich.academic.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findBySeccionId(Long seccionId);
    boolean existsBySeccionIdAndEstudianteUsername(Long seccionId, String estudianteUsername);
    List<Inscripcion> findByEstudianteUsername(String estudianteUsername);
}
