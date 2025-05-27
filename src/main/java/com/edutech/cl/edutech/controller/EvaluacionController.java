package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.service.DocenteService;
import com.edutech.cl.edutech.service.EvaluacionService;
import com.edutech.cl.edutech.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/evaluaciones")
@CrossOrigin(origins = "*")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Evaluacion>> getAllEvaluaciones() {
        return ResponseEntity.ok(evaluacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> getEvaluacionById(@PathVariable Integer id) {
        return evaluacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/curso/{cursoId}/docente/{docenteId}")
    public ResponseEntity<?> crearEvaluacion(@PathVariable Integer cursoId,
                                           @PathVariable Integer docenteId,
                                           @RequestBody Evaluacion evaluacion) {
        Optional<Curso> cursoOpt = cursoService.findById(cursoId);
        Optional<Docente> docenteOpt = docenteService.findById(docenteId);

        if (cursoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Curso no encontrado"));
        }

        if (docenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Docente no encontrado"));
        }

        Docente docente = docenteOpt.get();
        Curso curso = cursoOpt.get();

        // Verificar que el docente está asignado al curso
        if (curso.getDocente() == null || !curso.getDocente().getId_docente().equals(docenteId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El docente no está asignado a este curso"));
        }

        evaluacion.setCurso(curso);
        evaluacion.setDocente(docente);

        Evaluacion nuevaEvaluacion = evaluacionService.save(evaluacion);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Evaluación creada exitosamente");
        response.put("evaluacion", nuevaEvaluacion);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvaluacion(@PathVariable Integer id, @RequestBody Evaluacion evaluacion) {
        Optional<Evaluacion> evaluacionExistente = evaluacionService.findById(id);

        if (evaluacionExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Evaluacion eval = evaluacionExistente.get();
        eval.setPuntaje_maximo(evaluacion.getPuntaje_maximo());

        return ResponseEntity.ok(evaluacionService.save(eval));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluacion(@PathVariable Integer id) {
        if (evaluacionService.findById(id).isPresent()) {
            evaluacionService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Evaluación eliminada correctamente"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{evaluacionId}/docente")
    public ResponseEntity<Docente> getDocenteByEvaluacion(@PathVariable Integer evaluacionId) {
        Optional<Evaluacion> evaluacion = evaluacionService.findById(evaluacionId);
        if (evaluacion.isPresent() && evaluacion.get().getDocente() != null) {
            return ResponseEntity.ok(evaluacion.get().getDocente());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cursos/{cursoId}")
    public ResponseEntity<List<Evaluacion>> getEvaluacionesByCurso(@PathVariable Integer cursoId) {
        Optional<Curso> curso = cursoService.findById(cursoId);
        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get().getEvaluaciones());
        }
        return ResponseEntity.notFound().build();
    }
}
