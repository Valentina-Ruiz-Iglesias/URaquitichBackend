package com.raquitich.academic.service;

import com.raquitich.academic.dto.InscripcionRequest;
import com.raquitich.academic.dto.SeccionRequest;
import com.raquitich.academic.dto.SeccionResponse;
import com.raquitich.academic.model.Asignatura;
import com.raquitich.academic.model.Inscripcion;
import com.raquitich.academic.model.Seccion;
import com.raquitich.academic.repository.AsignaturaRepository;
import com.raquitich.academic.repository.HorarioRepository;
import com.raquitich.academic.repository.InscripcionRepository;
import com.raquitich.academic.repository.SeccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SeccionService - Pruebas Unitarias")
class SeccionServiceTest {

    @Mock private SeccionRepository seccionRepo;
    @Mock private AsignaturaRepository asignaturaRepo;
    @Mock private HorarioRepository horarioRepo;
    @Mock private InscripcionRepository inscripcionRepo;

    @InjectMocks
    private SeccionService service;

    private Asignatura asignatura;
    private Seccion seccion;
    private SeccionRequest request;

    @BeforeEach
    void setUp() {
        asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCodigo("MAT101");
        asignatura.setNombre("Matemáticas I");
        asignatura.setActiva(true);

        seccion = new Seccion();
        seccion.setId(1L);
        seccion.setAsignatura(asignatura);
        seccion.setPeriodo("2026-1");
        seccion.setCupoMaximo(30);
        seccion.setDocenteUsername("fcana");
        seccion.setActiva(true);
        seccion.setInscripciones(new ArrayList<>());
        seccion.setHorarios(new ArrayList<>());

        request = new SeccionRequest();
        request.setAsignaturaId(1L);
        request.setPeriodo("2026-1");
        request.setCupoMaximo(30);
        request.setDocenteUsername("fcana");
    }

    // ── listar ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("listar() retorna todas las secciones")
    void listar_retornaTodasLasSecciones() {
        when(seccionRepo.findAll()).thenReturn(List.of(seccion));

        List<SeccionResponse> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("2026-1", resultado.get(0).getPeriodo());
        assertEquals("fcana", resultado.get(0).getDocenteUsername());
    }

    @Test
    @DisplayName("listar() retorna lista vacía cuando no hay secciones")
    void listar_retornaVacio() {
        when(seccionRepo.findAll()).thenReturn(List.of());

        assertTrue(service.listar().isEmpty());
    }

    // ── obtener ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtener() retorna seccion cuando el ID existe")
    void obtener_idExiste_retornaSeccion() {
        when(seccionRepo.findById(1L)).thenReturn(Optional.of(seccion));

        SeccionResponse resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("fcana", resultado.getDocenteUsername());
    }

    @Test
    @DisplayName("obtener() lanza excepción cuando el ID no existe")
    void obtener_idNoExiste_lanzaExcepcion() {
        when(seccionRepo.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.obtener(99L)
        );
        assertEquals("Sección no encontrada", ex.getMessage());
    }

    // ── crear ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crear() crea una nueva sección correctamente")
    void crear_datosValidos_creaSeccion() {
        when(asignaturaRepo.findById(1L)).thenReturn(Optional.of(asignatura));
        when(seccionRepo.save(any(Seccion.class))).thenReturn(seccion);

        SeccionResponse resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("fcana", resultado.getDocenteUsername());
        assertEquals("2026-1", resultado.getPeriodo());
        verify(seccionRepo).save(any(Seccion.class));
    }

    @Test
    @DisplayName("crear() lanza excepción cuando la asignatura no existe")
    void crear_asignaturaNoExiste_lanzaExcepcion() {
        when(asignaturaRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.crear(request));
        verify(seccionRepo, never()).save(any());
    }

    @Test
    @DisplayName("crear() usa cupo por defecto 40 cuando no se especifica")
    void crear_sinCupo_usaDefecto40() {
        request.setCupoMaximo(null);
        when(asignaturaRepo.findById(1L)).thenReturn(Optional.of(asignatura));
        when(seccionRepo.save(any(Seccion.class))).thenAnswer(inv -> {
            Seccion s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        service.crear(request);

        verify(seccionRepo).save(argThat(s -> s.getCupoMaximo() == 40));
    }

    // ── desactivar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("desactivar() pone activa=false en la sección")
    void desactivar_seccionActiva_desactiva() {
        when(seccionRepo.findById(1L)).thenReturn(Optional.of(seccion));
        when(seccionRepo.save(any())).thenReturn(seccion);

        service.desactivar(1L);

        assertFalse(seccion.isActiva());
        verify(seccionRepo).save(seccion);
    }

    @Test
    @DisplayName("desactivar() lanza excepción cuando el ID no existe")
    void desactivar_idNoExiste_lanzaExcepcion() {
        when(seccionRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.desactivar(99L));
        verify(seccionRepo, never()).save(any());
    }

    // ── seccionesDelDocente ────────────────────────────────────────────────────

    @Test
    @DisplayName("seccionesDelDocente() retorna solo secciones activas del docente")
    void seccionesDelDocente_retornaActivas() {
        Seccion inactiva = new Seccion();
        inactiva.setId(2L);
        inactiva.setAsignatura(asignatura);
        inactiva.setPeriodo("2025-2");
        inactiva.setDocenteUsername("fcana");
        inactiva.setActiva(false);
        inactiva.setHorarios(new ArrayList<>());
        inactiva.setInscripciones(new ArrayList<>());

        when(seccionRepo.findByDocenteUsername("fcana")).thenReturn(List.of(seccion, inactiva));

        List<SeccionResponse> resultado = service.seccionesDelDocente("fcana");

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    @DisplayName("seccionesDelDocente() retorna vacío cuando el docente no tiene secciones")
    void seccionesDelDocente_sinSecciones_retornaVacio() {
        when(seccionRepo.findByDocenteUsername("otro")).thenReturn(List.of());

        assertTrue(service.seccionesDelDocente("otro").isEmpty());
    }

    // ── inscribir ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("inscribir() lanza excepción cuando el estudiante ya está inscrito")
    void inscribir_estudianteYaInscrito_lanzaExcepcion() {
        InscripcionRequest req = new InscripcionRequest();
        req.setEstudianteUsername("juan123");

        when(seccionRepo.findById(1L)).thenReturn(Optional.of(seccion));
        when(inscripcionRepo.existsBySeccionIdAndEstudianteUsername(1L, "juan123")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.inscribir(1L, req)
        );
        assertTrue(ex.getMessage().contains("ya está inscrito"));
    }

    @Test
    @DisplayName("inscribir() lanza excepción cuando la sección está llena")
    void inscribir_cupoLleno_lanzaExcepcion() {
        seccion.setCupoMaximo(1);

        Inscripcion inscrita = new Inscripcion();
        inscrita.setActiva(true);
        seccion.setInscripciones(List.of(inscrita));

        InscripcionRequest req = new InscripcionRequest();
        req.setEstudianteUsername("nuevo");

        when(seccionRepo.findById(1L)).thenReturn(Optional.of(seccion));
        when(inscripcionRepo.existsBySeccionIdAndEstudianteUsername(1L, "nuevo")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.inscribir(1L, req)
        );
        assertTrue(ex.getMessage().contains("cupo máximo"));
    }
}
