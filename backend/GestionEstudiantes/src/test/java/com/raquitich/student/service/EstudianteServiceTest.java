package com.raquitich.student.service;

import com.raquitich.student.client.AuthServiceClient;
import com.raquitich.student.dto.EstudianteRequest;
import com.raquitich.student.dto.EstudianteResponse;
import com.raquitich.student.dto.EstudianteUpdateRequest;
import com.raquitich.student.model.Estudiante;
import com.raquitich.student.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EstudianteService - Pruebas Unitarias")
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private EstudianteService service;

    private Estudiante estudianteActivo;
    private Estudiante estudianteInactivo;

    @BeforeEach
    void setUp() {
        estudianteActivo = new Estudiante();
        estudianteActivo.setUsername("juan.perez");
        estudianteActivo.setEmail("juan@raquitich.cl");
        estudianteActivo.setNombre("Juan Pérez");
        estudianteActivo.setRut("12345678-9");
        estudianteActivo.setCarrera("Ingeniería en Informática");
        estudianteActivo.setNumeroMatricula("MAT-2026-001");
        estudianteActivo.setAnioIngreso(2024);
        estudianteActivo.setActivo(true);

        estudianteInactivo = new Estudiante();
        estudianteInactivo.setUsername("ex.alumno");
        estudianteInactivo.setEmail("ex@raquitich.cl");
        estudianteInactivo.setNombre("Ex Alumno");
        estudianteInactivo.setActivo(false);
    }

    /**
     * Prueba 1 — listarEstudiantes solo retorna activos.
     *
     * Regla de negocio: los estudiantes desactivados (bajas, retiros) NO deben
     * aparecer en el listado general. Solo los que tienen activo=true son visibles.
     *
     * Si este test falla significa que la lista mostraría estudiantes que ya
     * no pertenecen a la institución junto con los vigentes.
     */
    @Test
    @DisplayName("listarEstudiantes() retorna solo estudiantes con activo=true")
    void listarEstudiantes_soloRetornaActivos() {
        when(estudianteRepository.findByActivoTrue()).thenReturn(List.of(estudianteActivo));

        List<EstudianteResponse> resultado = service.listarEstudiantes();

        assertEquals(1, resultado.size());
        assertEquals("juan.perez", resultado.get(0).getUsername());
        assertTrue(resultado.get(0).isActivo());
        verify(estudianteRepository, times(1)).findByActivoTrue();
        verify(estudianteRepository, never()).findAll();
    }

    /**
     * Prueba 2 — crearEstudiante rechaza email duplicado sin tocar ningún servicio externo.
     *
     * Regla de negocio crítica: si el email ya existe en db_estudiantes, hay que
     * abortar ANTES de llamar a ServicioAutenticacion (puerto 8081).
     * Si no se valida aquí, quedaría un usuario creado en ServicioAutenticacion
     * pero sin perfil en GestionEstudiantes — datos inconsistentes entre bases de datos.
     */
    @Test
    @DisplayName("crearEstudiante() lanza excepción y no llama a AuthService si el email ya existe")
    void crearEstudiante_emailDuplicado_abortaAntesDeCrearCredenciales() {
        EstudianteRequest request = new EstudianteRequest();
        request.setUsername("nuevo.usuario");
        request.setEmail("juan@raquitich.cl");
        request.setPassword("Password123!");
        request.setNombre("Nuevo Usuario");

        when(estudianteRepository.existsByEmail("juan@raquitich.cl")).thenReturn(true);

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.crearEstudiante(request)
        );

        assertEquals("Ya existe un estudiante con ese email", ex.getMessage());
        verify(authServiceClient, never()).crearCredenciales(any(), any(), any(), any(), any());
        verify(estudianteRepository, never()).save(any());
    }

    /**
     * Prueba 3 — desactivarEstudiante aplica borrado suave, no elimina de la BD.
     *
     * Regla de negocio: cuando se da de baja a un estudiante (retiro, egreso),
     * sus datos deben PERMANECER en la base de datos para mantener historial académico.
     * Solo se cambia activo=false. Nunca se usa delete().
     *
     * Si este test falla significa que se están borrando registros de estudiantes
     * y se perdería el historial de inscripciones, notas y observaciones.
     */
    @Test
    @DisplayName("desactivarEstudiante() pone activo=false y guarda sin eliminar el registro")
    void desactivarEstudiante_borradoSuave_mantieneRegistroEnBD() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudianteActivo));
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudianteActivo);

        service.desactivarEstudiante(1L);

        assertFalse(estudianteActivo.isActivo());
        verify(estudianteRepository, times(1)).save(estudianteActivo);
        verify(estudianteRepository, never()).delete(any());
        verify(estudianteRepository, never()).deleteById(any());
    }

    /**
     * Prueba 4 — actualizarEstudiante rechaza RUT duplicado de otro estudiante.
     *
     * Regla de negocio: dos estudiantes no pueden tener el mismo RUT.
     * Al actualizar, se verifica que el nuevo RUT no pertenezca a OTRO estudiante.
     * Si el RUT es el mismo que ya tiene el estudiante, se permite (no es duplicado).
     * Si falla esta prueba, un directivo podría asignar el RUT de otro alumno
     * por error, corrompiendo la identidad de ambos registros.
     */
    @Test
    @DisplayName("actualizarEstudiante() lanza excepción si el nuevo RUT ya pertenece a otro estudiante")
    void actualizarEstudiante_rutDuplicadoDeOtro_lanzaExcepcion() {
        estudianteActivo.setRut("12345678-9");

        EstudianteUpdateRequest updateRequest = new EstudianteUpdateRequest();
        updateRequest.setNombre("Juan Pérez");
        updateRequest.setRut("99999999-9");

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudianteActivo));
        when(estudianteRepository.existsByRut("99999999-9")).thenReturn(true);

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.actualizarEstudiante(1L, updateRequest)
        );

        assertEquals("Ya existe un estudiante con ese RUT", ex.getMessage());
        verify(estudianteRepository, never()).save(any());
    }

    /**
     * Prueba 5 — obtenerPorUsername lanza excepción si el username no existe.
     *
     * Regla de negocio: el endpoint /perfil devuelve los datos del estudiante
     * autenticado usando su username del token JWT. Si el username del token
     * no tiene perfil en esta BD (ej: se creó la credencial pero falló el registro
     * del perfil), el sistema debe responder con un error claro en vez de
     * retornar null y provocar un NullPointerException en el frontend.
     */
    @Test
    @DisplayName("obtenerPorUsername() lanza excepción cuando el username no tiene perfil registrado")
    void obtenerPorUsername_noExiste_lanzaExcepcionConMensajeCorrecto() {
        when(estudianteRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
            RuntimeException.class,
            () -> service.obtenerPorUsername("fantasma")
        );

        assertEquals("Perfil no encontrado", ex.getMessage());
    }
}
