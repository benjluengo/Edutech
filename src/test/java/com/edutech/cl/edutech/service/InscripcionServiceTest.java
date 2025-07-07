package com.edutech.cl.edutech.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.repository.InscripcionRepository;

class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;
    private Usuario usuario;
    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId_usuario(1);
        usuario.setNombre_usuario("Carlos");
        usuario.setApellido_usuario("Sánchez");
        usuario.setRun("12345678-9");

        curso = new Curso();
        curso.setId_curso(1);
        curso.setNombre_curso("Programación Python");
        curso.setDescripcion("Curso básico de Python");
        curso.setPrecio(90000.0);

        inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(1);
        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);
        inscripcion.setFecha_inscripcion(new Date());
    }

    @Test
    void whenFindAll_thenReturnInscripcionList() {
        when(inscripcionRepository.findAll()).thenReturn(Arrays.asList(inscripcion));
        
        List<Inscripcion> found = inscripcionService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(inscripcionRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnInscripcion() {
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        
        Optional<Inscripcion> found = inscripcionService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(inscripcion.getId_inscripcion(), found.get().getId_inscripcion());
        verify(inscripcionRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnInscripcion() {
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);
        
        Inscripcion saved = inscripcionService.save(inscripcion);
        
        assertNotNull(saved);
        assertEquals(inscripcion.getId_inscripcion(), saved.getId_inscripcion());
        verify(inscripcionRepository).save(inscripcion);
    }

    @Test
    void whenBuscarPorUserId_thenReturnInscripcionList() {
        when(inscripcionRepository.buscarPorUserId(1)).thenReturn(Arrays.asList(inscripcion));
        
        List<Inscripcion> found = inscripcionService.buscarPorUserId(1);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(1, found.get(0).getUsuario().getId_usuario());
        verify(inscripcionRepository).buscarPorUserId(1);
    }

    @Test
    void whenBuscarPorCursoId_thenReturnInscripcionList() {
        when(inscripcionRepository.buscarPorCursoId(1)).thenReturn(Arrays.asList(inscripcion));
        
        List<Inscripcion> found = inscripcionService.buscarPorCursoId(1);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(1, found.get(0).getCurso().getId_curso());
        verify(inscripcionRepository).buscarPorCursoId(1);
    }

    @Test
    void whenContarPorCursoId_thenReturnInteger() {
        when(inscripcionRepository.contarPorCursoId(1)).thenReturn(5);
        
        Integer count = inscripcionService.contarPorCursoId(1);
        
        assertEquals(5, count);
        verify(inscripcionRepository).contarPorCursoId(1);
    }

    @Test
    void whenExistePorUserIdYCursoId_thenReturnBoolean() {
        when(inscripcionRepository.existePorUserIdYCursoId(1, 1)).thenReturn(true);
        
        boolean exists = inscripcionService.existePorUserIdYCursoId(1, 1);
        
        assertTrue(exists);
        verify(inscripcionRepository).existePorUserIdYCursoId(1, 1);
    }
}
