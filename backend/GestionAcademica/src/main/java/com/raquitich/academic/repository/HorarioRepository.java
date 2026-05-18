package com.raquitich.academic.repository;

import com.raquitich.academic.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findBySeccionId(Long seccionId);
}
