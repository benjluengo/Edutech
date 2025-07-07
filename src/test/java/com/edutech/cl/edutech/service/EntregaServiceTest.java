package com.edutech.cl.edutech.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.repository.EntregaRepository;
import com.edutech.cl.edutech.repository.UsuarioRepository;
import com.edutech.cl.edutech.repository.EvaluacionRepository;

class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private EntregaService entregaService;

    private Entrega entrega;
    private Usuario usuario;
    private Evaluacion evaluacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId_usuario(1);
        usuario.setNombre_usuario("Ana");
        usuario.setApellido_usuario("Gómez");

        evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(1);
        evaluacion.setPuntaje_maximo(100);

        entrega = new Entrega();
        entrega.setId_entrega(1);
        entrega.setUsuario(usuario);
        entrega.setEvaluacion(evaluacion);
        entrega.setContenido_entrega("Contenido de prueba");
        entrega.setEstado_entrega("Pendiente");
    }

    @Test
    void whenFindAll_thenReturnEntregaList() {
        when(entregaRepository.findAll()).thenReturn(Arrays.asList(entrega));
        
        List<Entrega> found = entregaService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(entregaRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnEntrega() {
        when(entregaRepository.findById(1)).thenReturn(Optional.of(entrega));
        
        Optional<Entrega> found = entregaService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(entrega.getContenido_entrega(), found.get().getContenido_entrega());
        verify(entregaRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnEntrega() {
        when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);
        
        Entrega saved = entregaService.save(entrega);
        
        assertNotNull(saved);
        assertEquals(entrega.getContenido_entrega(), saved.getContenido_entrega());
        verify(entregaRepository).save(entrega);
    }

    @Test
    void whenCrearEntrega_thenReturnEntrega() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(evaluacionRepository.findById(1)).thenReturn(Optional.of(evaluacion));
        when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);
        
        Entrega created = entregaService.crearEntrega(1, 1, "Contenido de prueba");
        
        assertNotNull(created);
        assertEquals("Contenido de prueba", created.getContenido_entrega());
        assertEquals("Pendiente", created.getEstado_entrega());
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void whenActualizarEstadoEntrega_thenReturnEntrega() {
        String nuevoEstado = "Revisado";
        when(entregaRepository.findById(1)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);
        
        Entrega updated = entregaService.actualizarEstadoEntrega(1, nuevoEstado);
        
        assertNotNull(updated);
        assertEquals(nuevoEstado, updated.getEstado_entrega());
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void whenFindByUsuarioId_thenReturnEntregaList() {
        when(entregaRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(entrega));
        
        List<Entrega> found = entregaService.findByUsuarioId(1);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(entregaRepository).findByUsuarioId(1);
    }

    @Test
    void whenFindByEvaluacionId_thenReturnEntregaList() {
        when(entregaRepository.findByEvaluacionId(1)).thenReturn(Arrays.asList(entrega));
        
        List<Entrega> found = entregaService.findByEvaluacionId(1);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(entregaRepository).findByEvaluacionId(1);
    }

    @Test
    void whenFindByEstado_thenReturnEntregaList() {
        when(entregaRepository.findByEstado("Pendiente")).thenReturn(Arrays.asList(entrega));
        
        List<Entrega> found = entregaService.findByEstado("Pendiente");
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("Pendiente", found.get(0).getEstado_entrega());
        verify(entregaRepository).findByEstado("Pendiente");
    }
}
