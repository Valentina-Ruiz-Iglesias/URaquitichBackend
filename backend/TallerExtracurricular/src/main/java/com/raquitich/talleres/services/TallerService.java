package com.raquitich.talleres.services;

import com.raquitich.talleres.dtos.TallerRequestDTO;
import com.raquitich.talleres.dtos.TallerResponseDTO;

import java.util.List;

public interface TallerService {

    List<TallerResponseDTO> listarTalleres();

    TallerResponseDTO obtenerTallerPorId(Long id);

    TallerResponseDTO crearTaller(TallerRequestDTO request);

    TallerResponseDTO actualizarTaller(Long id, TallerRequestDTO request);

    void eliminarTaller(Long id);

    TallerResponseDTO inscribirEstudiante(Long id);
}
