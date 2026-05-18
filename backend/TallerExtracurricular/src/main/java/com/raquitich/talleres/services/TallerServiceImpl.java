package com.raquitich.talleres.services;

import com.raquitich.talleres.dtos.TallerRequestDTO;
import com.raquitich.talleres.dtos.TallerResponseDTO;
import com.raquitich.talleres.exceptions.CuposInsuficientesException;
import com.raquitich.talleres.exceptions.TallerNotFoundException;
import com.raquitich.talleres.models.TallerExtracurricular;
import com.raquitich.talleres.repositories.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TallerServiceImpl implements TallerService {

    private final TallerRepository tallerRepository;

    @Override
    public List<TallerResponseDTO> listarTalleres() {
        return tallerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TallerResponseDTO obtenerTallerPorId(Long id) {
        TallerExtracurricular taller = buscarTallerOLanzarError(id);
        return mapToResponseDTO(taller);
    }

    @Override
    @Transactional
    public TallerResponseDTO crearTaller(TallerRequestDTO request) {
        TallerExtracurricular taller = TallerExtracurricular.builder()
                .nombreTaller(request.getNombreTaller())
                .descripcionTaller(request.getDescripcionTaller())
                .cuposTaller(request.getCuposTaller())
                .build();

        TallerExtracurricular guardado = tallerRepository.save(taller);
        return mapToResponseDTO(guardado);
    }

    @Override
    @Transactional
    public TallerResponseDTO actualizarTaller(Long id, TallerRequestDTO request) {
        TallerExtracurricular taller = buscarTallerOLanzarError(id);

        taller.setNombreTaller(request.getNombreTaller());
        taller.setDescripcionTaller(request.getDescripcionTaller());
        taller.setCuposTaller(request.getCuposTaller());

        TallerExtracurricular actualizado = tallerRepository.save(taller);
        return mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarTaller(Long id) {
        buscarTallerOLanzarError(id);
        tallerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TallerResponseDTO inscribirEstudiante(Long id) {
        TallerExtracurricular taller = buscarTallerOLanzarError(id);

        if (taller.getCuposTaller() <= 0) {
            throw new CuposInsuficientesException(id);
        }

        taller.setCuposTaller(taller.getCuposTaller() - 1);
        TallerExtracurricular actualizado = tallerRepository.save(taller);
        return mapToResponseDTO(actualizado);
    }

    private TallerExtracurricular buscarTallerOLanzarError(Long id) {
        return tallerRepository.findById(id)
                .orElseThrow(() -> new TallerNotFoundException(id));
    }

    private TallerResponseDTO mapToResponseDTO(TallerExtracurricular taller) {
        return TallerResponseDTO.builder()
                .idTaller(taller.getIdTaller())
                .nombreTaller(taller.getNombreTaller())
                .descripcionTaller(taller.getDescripcionTaller())
                .cuposTaller(taller.getCuposTaller())
                .build();
    }
}
