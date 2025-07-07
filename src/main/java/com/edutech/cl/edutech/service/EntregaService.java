package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.repository.EntregaRepository;
import com.edutech.cl.edutech.repository.UsuarioRepository;
import com.edutech.cl.edutech.repository.EvaluacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntregaService {
    
    @Autowired
    private EntregaRepository entregaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public List<Entrega> findAll() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> findById(Integer id) {
        return entregaRepository.findById(id);
    }

    public Entrega save(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    public void deleteById(Integer id) {
        entregaRepository.deleteById(id);
    }

    public List<Entrega> findByUsuarioId(Integer userId) {
        return entregaRepository.findByUsuarioId(userId);
    }

    public List<Entrega> findByEvaluacionId(Integer evaluacionId) {
        return entregaRepository.findByEvaluacionId(evaluacionId);
    }

    public List<Entrega> findByEstado(String estado) {
        return entregaRepository.findByEstado(estado);
    }

    public Entrega crearEntrega(Integer userId, Integer evaluacionId, String contenido) {
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId).orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));

        Entrega entrega = new Entrega();
        entrega.setUsuario(usuario);
        entrega.setEvaluacion(evaluacion);
        entrega.setContenido_entrega(contenido);
        entrega.setEstado_entrega("PENDIENTE");

        return entregaRepository.save(entrega);
    }

    public Entrega actualizarEstadoEntrega(Integer entregaId, String nuevoEstado) {
        Entrega entrega = entregaRepository.findById(entregaId).orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        
        entrega.setEstado_entrega(nuevoEstado);
        return entregaRepository.save(entrega);
    }
}
