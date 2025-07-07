package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> findById(Integer id) {
        return cursoRepository.findById(id);
    }

    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void deleteById(Integer id) {
        cursoRepository.deleteById(id);
    }

    // Métodos específicos personalizados
    public List<Curso> buscarPorNombre(String nombre) {
        return cursoRepository.buscarPorNombre(nombre);
    }

    public List<Curso> buscarPorDocente(Integer docenteId) {
        return cursoRepository.buscarPorDocente(docenteId);
    }

    public Integer contarEstudiantesPorCurso(Integer cursoId) {
        return cursoRepository.contarEstudiantesPorCurso(cursoId);
    }

    public List<Curso> buscarCursosDisponibles() {
        return cursoRepository.buscarCursosDisponibles();
    }

    public List<Curso> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return cursoRepository.buscarPorRangoPrecio(precioMin, precioMax);
    }
}
