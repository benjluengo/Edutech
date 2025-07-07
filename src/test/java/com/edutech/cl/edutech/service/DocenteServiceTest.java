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

import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.repository.DocenteRepository;

class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @InjectMocks
    private DocenteService docenteService;

    private Docente docente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        docente = new Docente();
        docente.setId_docente(1);
        docente.setNombre_docente("Juan");
        docente.setApellido_docente("Pérez");
    }

    @Test
    void whenFindAll_thenReturnDocenteList() {
        when(docenteRepository.findAll()).thenReturn(Arrays.asList(docente));
        
        List<Docente> found = docenteService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(docenteRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnDocente() {
        when(docenteRepository.findById(1)).thenReturn(Optional.of(docente));
        
        Optional<Docente> found = docenteService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(docente.getNombre_docente(), found.get().getNombre_docente());
        verify(docenteRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnDocente() {
        when(docenteRepository.save(any(Docente.class))).thenReturn(docente);
        
        Docente saved = docenteService.save(docente);
        
        assertNotNull(saved);
        assertEquals(docente.getNombre_docente(), saved.getNombre_docente());
        verify(docenteRepository).save(docente);
    }

    @Test
    void whenDeleteById_thenVerifyRepositoryCall() {
        doNothing().when(docenteRepository).deleteById(1);
        
        docenteService.deleteById(1);
        
        verify(docenteRepository).deleteById(1);
    }
}
