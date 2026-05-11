package com.raquitich.student.repository;

import com.raquitich.student.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByNumeroMatricula(String numeroMatricula);

    Optional<Estudiante> findByUsername(String username);
    Optional<Estudiante> findByEmail(String email);

    List<Estudiante> findByActivoTrue();
    List<Estudiante> findByCarrera(String carrera);
}
