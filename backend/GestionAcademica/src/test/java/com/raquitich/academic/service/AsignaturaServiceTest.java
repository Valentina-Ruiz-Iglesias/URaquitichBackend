package com.raquitich.academic.service;

import com.raquitich.academic.dto.AsignaturaRequest;
import com.raquitich.academic.dto.AsignaturaResponse;
import com.raquitich.academic.model.Asignatura;
import com.raquitich.academic.repository.AsignaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("AsignaturaService - Pruebas Unitarias")
class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository repo;

    @InjectMocks
    private AsignaturaService service;

    private Asignatura asignatura;
    private AsignaturaRequest request;

    @BeforeEach
    void setUp() {
        asignatura = new Asignatura();
        asignatura.setId(1L);
        asignatura.setCodigo("MAT101");
        asignatura.setNombre("Matemáticas I");
        asignatura.setDescripcion("Algebra y calculo basico");
        asignatura.setCreditos(3);
        asignatura.setActiva(true);

        request = new AsignaturaRequest();
        request.setCodigo("MAT101");
        request.setNombre("Matemáticas I");
        request.setDescripcion("Algebra y calculo basico");
        request.setCreditos(3);
    }

    // ── listar ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("listar() retorna lista de asignaturas correctamente")
    void listar_retornaLista() {
        when(repo.findAll()).thenReturn(List.of(asignatura));

        List<AsignaturaResponse> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("MAT101", resultado.get(0).getCodigo());
        assertEquals("Matemáticas I", resultado.get(0).getNombre());
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("listar() retorna lista vacía cuando no hay asignaturas")
    void listar_retornaVacio() {
        when(repo.findAll()).thenReturn(List.of());

        List<AsignaturaResponse> resultado = service.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ── obtener ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtener() retorna asignatura cuando existe el ID")
    void obtener_existeId_retornaAsignatura() {
        when(repo.findById(1L)).thenReturn(Optional.of(asignatura));

        AsignaturaResponse resultado = service.obtener(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("MAT101", resultado.getCodigo());
        assertTrue(resultado.isActiva());
    }

    @Test
    @DisplayName("obtener() lanza excepción cuando el ID no existe")
    void obtener_noExisteId_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.obtener(99L)
        );

        assertEquals("Asignatura no encontrada", ex.getMessage());
    }

    // ── crear ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crear() crea asignatura nueva correctamente")
    void crear_nuevaAsignatura_retornaResponse() {
        when(repo.existsByCodigo("MAT101")).thenReturn(false);
        when(repo.save(any(Asignatura.class))).thenReturn(asignatura);

        AsignaturaResponse resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("MAT101", resultado.getCodigo());
        assertEquals("Matemáticas I", resultado.getNombre());
        verify(repo, times(1)).save(any(Asignatura.class));
    }

    @Test
    @DisplayName("crear() lanza excepción cuando el código ya existe")
    void crear_codigoDuplicado_lanzaExcepcion() {
        when(repo.existsByCodigo("MAT101")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.crear(request)
        );

        assertTrue(ex.getMessage().contains("MAT101"));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("crear() guarda el código en mayúsculas y sin espacios")
    void crear_codigoEnMayusculas() {
        request.setCodigo("  mat101  ");
        when(repo.existsByCodigo("MAT101")).thenReturn(false);
        when(repo.save(any(Asignatura.class))).thenAnswer(inv -> {
            Asignatura saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        service.crear(request);

        // El servicio hace toUpperCase().trim() antes de guardar
        verify(repo).save(argThat(a -> "MAT101".equals(a.getCodigo())));
    }

    // ── actualizar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizar() modifica nombre cuando se proporciona")
    void actualizar_nombreNuevo_actualizaCorrectamente() {
        AsignaturaRequest updateReq = new AsignaturaRequest();
        updateReq.setNombre("Matemáticas Avanzadas");

        when(repo.findById(1L)).thenReturn(Optional.of(asignatura));
        when(repo.save(any(Asignatura.class))).thenReturn(asignatura);

        AsignaturaResponse resultado = service.actualizar(1L, updateReq);

        assertNotNull(resultado);
        verify(repo, times(1)).save(any(Asignatura.class));
    }

    @Test
    @DisplayName("actualizar() lanza excepción cuando el ID no existe")
    void actualizar_noExisteId_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> service.actualizar(99L, request)
        );
    }

    // ── desactivar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("desactivar() pone activa=false en la asignatura")
    void desactivar_asignaturaActiva_desactiva() {
        when(repo.findById(1L)).thenReturn(Optional.of(asignatura));
        when(repo.save(any(Asignatura.class))).thenReturn(asignatura);

        service.desactivar(1L);

        assertFalse(asignatura.isActiva());
        verify(repo, times(1)).save(asignatura);
    }

    @Test
    @DisplayName("desactivar() lanza excepción cuando el ID no existe")
    void desactivar_noExisteId_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> service.desactivar(99L)
        );

        verify(repo, never()).save(any());
    }
}
