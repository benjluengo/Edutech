package com.edutech.cl.edutech.service;

import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.repository.DocenteRepository;
import com.edutech.cl.edutech.repository.CursoRepository;
import com.edutech.cl.edutech.repository.EvaluacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DocenteService {
    
    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public List<Docente> findAll() {
        return docenteRepository.findAll();
    }

    public Optional<Docente> findById(Integer id) {
        return docenteRepository.findById(id);
    }

    public Docente save(Docente docente) {
        return docenteRepository.save(docente);
    }

    public void deleteById(Integer id) {
        docenteRepository.deleteById(id);
    }

    public boolean asignarDocenteACurso(Integer cursoId, Integer docenteId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        
        if (cursoOpt.isPresent() && docenteOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setDocente(docenteOpt.get());
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }

    public boolean eliminarDocenteDeCurso(Integer cursoId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setDocente(null);
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }

    public boolean asignarDocenteAEvaluacion(Integer evaluacionId, Integer docenteId) {
        Optional<Evaluacion> evaluacionOpt = evaluacionRepository.findById(evaluacionId);
        Optional<Docente> docenteOpt = docenteRepository.findById(docenteId);
        
        if (evaluacionOpt.isPresent() && docenteOpt.isPresent()) {
            Evaluacion evaluacion = evaluacionOpt.get();
            evaluacion.setDocente(docenteOpt.get());
            evaluacionRepository.save(evaluacion);
            return true;
        }
        return false;
    }
}
