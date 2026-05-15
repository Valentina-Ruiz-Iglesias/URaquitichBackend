package com.raquitich.observaciones.repository;

import com.raquitich.observaciones.model.Observacion;
import com.raquitich.observaciones.model.TipoObservacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacionRepository extends JpaRepository<Observacion, Long> {
    List<Observacion> findByEstudianteUsernameOrderByFechaObservacionDesc(String estudianteUsername);
    List<Observacion> findByAutorUsernameOrderByFechaObservacionDesc(String autorUsername);
    List<Observacion> findByEstudianteUsernameAndTipoOrderByFechaObservacionDesc(String estudianteUsername, TipoObservacion tipo);
    List<Observacion> findAllByOrderByFechaObservacionDesc();
}
