package com.raquitich.talleres.services;

import com.raquitich.talleres.dtos.TallerRequestDTO;
import com.raquitich.talleres.dtos.TallerResponseDTO;
import com.raquitich.talleres.exceptions.CuposInsuficientesException;
import com.raquitich.talleres.exceptions.TallerNotFoundException;
import com.raquitich.talleres.models.TallerExtracurricular;
import com.raquitich.talleres.repositories.TallerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TallerServiceTest {

    @Mock
    private TallerRepository tallerRepository;

    @InjectMocks
    private TallerServiceImpl tallerService;

    private TallerExtracurricular tallerConCupos;
    private TallerExtracurricular tallerSinCupos;

    @BeforeEach
    void setUp() {
        tallerConCupos = TallerExtracurricular.builder()
                .idTaller(1L)
                .nombreTaller("Taller de Robotica")
                .descripcionTaller("Aprende robotica basica")
                .cuposTaller(10)
                .build();

        tallerSinCupos = TallerExtracurricular.builder()
                .idTaller(2L)
                .nombreTaller("Taller de Arte")
                .descripcionTaller("Pintura y escultura")
                .cuposTaller(0)
                .build();
    }

    @Test
    @DisplayName("Debe crear un taller exitosamente")
    void deberia_crearTaller_exitosamente() {
        TallerRequestDTO request = TallerRequestDTO.builder()
                .nombreTaller("Taller de Robotica")
                .descripcionTaller("Aprende robotica basica")
                .cuposTaller(10)
                .build();

        when(tallerRepository.save(any(TallerExtracurricular.class)))
                .thenReturn(tallerConCupos);

        TallerResponseDTO resultado = tallerService.crearTaller(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTaller());
        assertEquals("Taller de Robotica", resultado.getNombreTaller());
        assertEquals(10, resultado.getCuposTaller());
        verify(tallerRepository, times(1)).save(any(TallerExtracurricular.class));
    }

    @Test
    @DisplayName("Debe inscribir estudiante y reducir cupos en 1")
    void deberia_inscribirEstudiante_exitosamente() {
        when(tallerRepository.findById(1L))
                .thenReturn(Optional.of(tallerConCupos));

        TallerExtracurricular tallerActualizado = TallerExtracurricular.builder()
                .idTaller(1L)
                .nombreTaller("Taller de Robotica")
                .descripcionTaller("Aprende robotica basica")
                .cuposTaller(9)
                .build();

        when(tallerRepository.save(any(TallerExtracurricular.class)))
                .thenReturn(tallerActualizado);

        TallerResponseDTO resultado = tallerService.inscribirEstudiante(1L);

        assertNotNull(resultado);
        assertEquals(9, resultado.getCuposTaller());
        verify(tallerRepository, times(1)).findById(1L);
        verify(tallerRepository, times(1)).save(any(TallerExtracurricular.class));
    }

    @Test
    @DisplayName("Debe lanzar CuposInsuficientesException si no hay cupos")
    void deberia_lanzarExcepcion_cuandoNoHayCupos() {
        when(tallerRepository.findById(2L))
                .thenReturn(Optional.of(tallerSinCupos));

        CuposInsuficientesException excepcion = assertThrows(
                CuposInsuficientesException.class,
                () -> tallerService.inscribirEstudiante(2L)
        );

        assertTrue(excepcion.getMessage().contains("2"));
        verify(tallerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar TallerNotFoundException si el ID no existe")
    void deberia_lanzarExcepcion_cuandoTallerNoExiste() {
        when(tallerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                TallerNotFoundException.class,
                () -> tallerService.obtenerTallerPorId(99L)
        );
    }
}
