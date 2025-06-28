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

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.repository.EvaluacionRepository;

class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @InjectMocks
    private EvaluacionService evaluacionService;

    private Evaluacion evaluacion;
    private Curso curso;
    private Docente docente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        docente = new Docente();
        docente.setId_docente(1);
        docente.setNombre_docente("María");
        docente.setApellido_docente("López");

        curso = new Curso();
        curso.setId_curso(1);
        curso.setNombre_curso("Matemáticas");
        curso.setDescripcion("Curso de matemáticas básicas");
        curso.setPrecio(80000.0);
        curso.setDocente(docente);

        evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(1);
        evaluacion.setPuntaje_maximo(100);
        evaluacion.setCurso(curso);
        evaluacion.setDocente(docente);
    }

    @Test
    void whenFindAll_thenReturnEvaluacionList() {
        when(evaluacionRepository.findAll()).thenReturn(Arrays.asList(evaluacion));
        
        List<Evaluacion> found = evaluacionService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(evaluacionRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnEvaluacion() {
        when(evaluacionRepository.findById(1)).thenReturn(Optional.of(evaluacion));
        
        Optional<Evaluacion> found = evaluacionService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(evaluacion.getPuntaje_maximo(), found.get().getPuntaje_maximo());
        verify(evaluacionRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnEvaluacion() {
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion);
        
        Evaluacion saved = evaluacionService.save(evaluacion);
        
        assertNotNull(saved);
        assertEquals(evaluacion.getPuntaje_maximo(), saved.getPuntaje_maximo());
        verify(evaluacionRepository).save(evaluacion);
    }

    @Test
    void whenDeleteById_thenVerifyRepositoryCall() {
        doNothing().when(evaluacionRepository).deleteById(1);
        
        evaluacionService.deleteById(1);
        
        verify(evaluacionRepository).deleteById(1);
    }
}
