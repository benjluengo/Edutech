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

import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.repository.UsuarioRepository;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario();
        usuario.setId_usuario(1);
        usuario.setNombre_usuario("Pedro");
        usuario.setApellido_usuario("Martínez");
        usuario.setRun("98765432-1");
        usuario.setContraseña("clave123");
    }

    @Test
    void whenFindAll_thenReturnUsuarioList() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        
        List<Usuario> found = usuarioService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnUsuario() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        
        Optional<Usuario> found = usuarioService.findById(1);
        
        assertTrue(found.isPresent());
        assertEquals(usuario.getNombre_usuario(), found.get().getNombre_usuario());
        verify(usuarioRepository).findById(1);
    }

    @Test
    void whenSave_thenReturnUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        Usuario saved = usuarioService.save(usuario);
        
        assertNotNull(saved);
        assertEquals(usuario.getNombre_usuario(), saved.getNombre_usuario());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void whenBuscarPorRun_thenReturnUsuario() {
        String run = "98765432-1";
        when(usuarioRepository.buscarPorRun(run)).thenReturn(usuario);
        
        Usuario found = usuarioService.buscarPorRun(run);
        
        assertNotNull(found);
        assertEquals(run, found.getRun());
        verify(usuarioRepository).buscarPorRun(run);
    }

    @Test
    void whenBuscarPorApellido_thenReturnUsuarioList() {
        String apellido = "Martínez";
        when(usuarioRepository.buscarPorApellido(apellido)).thenReturn(Arrays.asList(usuario));
        
        List<Usuario> found = usuarioService.buscarPorApellido(apellido);
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(apellido, found.get(0).getApellido_usuario());
        verify(usuarioRepository).buscarPorApellido(apellido);
    }
}
