package com.raquitich.academic.repository;

import com.raquitich.academic.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    boolean existsByCodigo(String codigo);
    Optional<Asignatura> findByCodigo(String codigo);
}
