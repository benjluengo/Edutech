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

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.repository.CursoRepository;

class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        curso = new Curso();
        curso.setId_curso(1);
        curso.setNombre_curso("Programación Java");
        curso.setDescripcion("Curso básico de Java");
        curso.setPrecio(100000.0);
    }

    @Test
    void whenFindAll_thenReturnCursoList() {
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso));
        
        List<Curso> found = cursoService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(cursoRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnCurso() {
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        
        Optional<Curso> found = cursoService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(curso.getNombre_curso(), found.get().getNombre_curso());
        verify(cursoRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnCurso() {
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        
        Curso saved = cursoService.save(curso);
        
        assertNotNull(saved);
        assertEquals(curso.getNombre_curso(), saved.getNombre_curso());
        verify(cursoRepository).save(curso);
    }

    @Test
    void whenBuscarPorNombre_thenReturnCursoList() {
        String nombre = "Programación Java";
        when(cursoRepository.buscarPorNombre(nombre)).thenReturn(Arrays.asList(curso));
        
        List<Curso> found = cursoService.buscarPorNombre(nombre);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(nombre, found.get(0).getNombre_curso());
        verify(cursoRepository).buscarPorNombre(nombre);
    }

    @Test
    void whenBuscarPorRangoPrecio_thenReturnCursoList() {
        Double precioMin = 50000.0;
        Double precioMax = 150000.0;
        when(cursoRepository.buscarPorRangoPrecio(precioMin, precioMax)).thenReturn(Arrays.asList(curso));
        
        List<Curso> found = cursoService.buscarPorRangoPrecio(precioMin, precioMax);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertTrue(found.get(0).getPrecio() >= precioMin && found.get(0).getPrecio() <= precioMax);
        verify(cursoRepository).buscarPorRangoPrecio(precioMin, precioMax);
    }
}
