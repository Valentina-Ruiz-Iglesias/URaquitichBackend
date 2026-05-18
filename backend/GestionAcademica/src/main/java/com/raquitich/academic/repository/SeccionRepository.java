package com.raquitich.academic.repository;

import com.raquitich.academic.model.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByAsignaturaId(Long asignaturaId);
    List<Seccion> findByDocenteUsername(String docenteUsername);
}
