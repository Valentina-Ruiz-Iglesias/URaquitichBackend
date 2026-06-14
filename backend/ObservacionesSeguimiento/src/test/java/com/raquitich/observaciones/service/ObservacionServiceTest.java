package com.raquitich.observaciones.service;

import com.raquitich.observaciones.dto.ObservacionRequest;
import com.raquitich.observaciones.repository.ObservacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObservacionServiceTest {

    // 1. EL MOCK: Fingimos ser la base de datos
    @Mock
    private ObservacionRepository repository;

    // 2. INJECT MOCKS: El servicio real que estamos evaluando
    @InjectMocks
    private ObservacionService service;

    @Test
    void eliminar_ObservacionExistente_DeberiaEliminarExitosamente() {
        // --- ARRANGE (Preparar) ---
        Long idObservacion = 1L;
        // Le decimos a la base de datos falsa que sí encontró el registro
        when(repository.existsById(idObservacion)).thenReturn(true);

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        // Verificamos que el método no lance ningún error al intentar borrar
        assertDoesNotThrow(() -> service.eliminar(idObservacion));
        
        // Verificamos que se haya llamado al método de borrado de la BD exactamente 1 vez
        verify(repository, times(1)).deleteById(idObservacion);
    }

    @Test
    void eliminar_ObservacionNoExistente_DeberiaLanzarExcepcion() {
        // --- ARRANGE (Preparar) ---
        Long idObservacion = 99L;
        // Le decimos a la base de datos falsa que NO encontró nada
        when(repository.existsById(idObservacion)).thenReturn(false);

        // --- ACT (Actuar) ---
        // Capturamos el error que el servicio debería lanzar
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.eliminar(idObservacion);
        });

        // --- ASSERT (Afirmar) ---
        // Confirmamos que el mensaje del error sea exactamente el programado
        assertEquals("Observación no encontrada con id: 99", exception.getMessage());
        // Confirmamos que la base de datos NUNCA intentó borrar nada
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void registrar_FaltaEstudiante_DeberiaLanzarExcepcion() {
        // --- ARRANGE (Preparar) ---
        // Creamos un request (petición) falso y simulamos que viene vacío
        ObservacionRequest requestFalso = mock(ObservacionRequest.class);
        when(requestFalso.getEstudianteUsername()).thenReturn(null);

        // --- ACT (Actuar) ---
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.registrar(requestFalso, "profe_admin");
        });

        // --- ASSERT (Afirmar) ---
        // Verificamos que el sistema se defienda y rechace la operación
        assertEquals("El username del estudiante es obligatorio", exception.getMessage());
        // Verificamos que la base de datos NUNCA guardó esta información inválida
        verify(repository, never()).save(any());
    }
}