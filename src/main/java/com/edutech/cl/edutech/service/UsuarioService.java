package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Métodos específicos personalizados
    public Usuario buscarPorRun(String run) {
        return usuarioRepository.buscarPorRun(run);
    }

    public List<Usuario> buscarPorApellido(String apellido) {
        return usuarioRepository.buscarPorApellido(apellido);
    }
}
