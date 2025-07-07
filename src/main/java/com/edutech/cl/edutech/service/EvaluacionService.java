package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.repository.EvaluacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvaluacionService {
    
    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public List<Evaluacion> findAll() {
        return evaluacionRepository.findAll();
    }

    public Optional<Evaluacion> findById(Integer id) {
        return evaluacionRepository.findById(id);
    }

    public Evaluacion save(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public void deleteById(Integer id) {
        evaluacionRepository.deleteById(id);
    }
}
