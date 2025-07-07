package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InscripcionService {
    
    @Autowired
    private InscripcionRepository inscripcionRepository;

    public List<Inscripcion> findAll() {
        return inscripcionRepository.findAll();
    }

    public Optional<Inscripcion> findById(Integer id) {
        return inscripcionRepository.findById(id);
    }

    public Inscripcion save(Inscripcion inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    public void deleteById(Integer id) {
        inscripcionRepository.deleteById(id);
    }

    // Métodos específicos personalizados
    public List<Inscripcion> buscarPorUserId(Integer userId) {
        return inscripcionRepository.buscarPorUserId(userId);
    }

    public List<Inscripcion> buscarPorCursoId(Integer cursoId) {
        return inscripcionRepository.buscarPorCursoId(cursoId);
    }

    public Integer contarPorCursoId(Integer cursoId) {
        return inscripcionRepository.contarPorCursoId(cursoId);
    }

    public List<Inscripcion> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        return inscripcionRepository.buscarPorFecha(fechaInicio, fechaFin);
    }

    public boolean existePorUserIdYCursoId(Integer userId, Integer cursoId) {
        return inscripcionRepository.existePorUserIdYCursoId(userId, cursoId);
    }

    // Método de validación de inscripción
    public boolean validarInscripcion(Integer userId, Integer cursoId) {
        if (existePorUserIdYCursoId(userId, cursoId)) {
            return false;
        }
        
        // Verificar si hay cupo en el curso
        Integer inscritos = contarPorCursoId(cursoId);
        return inscritos < 30;
    }

    public List<Inscripcion> findByCursoId(Integer cursoId) {
        return buscarPorCursoId(cursoId);
    }

    public List<Inscripcion> findByUsuarioId(Integer usuarioId) {
        return buscarPorUserId(usuarioId);
    }
}
