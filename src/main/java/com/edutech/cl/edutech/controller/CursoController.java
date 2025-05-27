package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private DocenteService docenteService;

    @GetMapping
    public ResponseEntity<List<Curso>> getAllCursos() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Integer id) {
        return cursoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso) {
        return new ResponseEntity<>(cursoService.save(curso), HttpStatus.CREATED);
    }

    @PostMapping("/docente/{docenteId}")
    public ResponseEntity<?> createCursoConDocente(@PathVariable Integer docenteId, @RequestBody Curso curso) {
        Optional<Docente> docente = docenteService.findById(docenteId);
        if (docente.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "El docente especificado no existe"));
        }
        
        curso.setDocente(docente.get());
        Curso cursoCreado = cursoService.save(curso);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Curso creado y asignado al docente correctamente");
        response.put("curso", cursoCreado);
        response.put("docente", Map.of(
            "id", docente.get().getId_docente(),
            "nombre", docente.get().getNombre_docente() + " " + docente.get().getApellido_docente()
        ));
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurso(@PathVariable Integer id, @RequestBody Curso curso) {
        Optional<Curso> cursoExistente = cursoService.findById(id);
        
        if (cursoExistente.isPresent()) {
            curso.setId_curso(id);
            return ResponseEntity.ok(cursoService.save(curso));
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Integer id) {
        if (cursoService.findById(id).isPresent()) {
            cursoService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Curso eliminado correctamente"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Curso>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(cursoService.buscarPorNombre(nombre));
    }

    @GetMapping("/precio")
    public ResponseEntity<List<Curso>> buscarPorPrecio(
            @RequestParam Double precioMin,
            @RequestParam Double precioMax) {
        return ResponseEntity.ok(cursoService.buscarPorRangoPrecio(precioMin, precioMax));
    }

    @GetMapping("/{id}/estudiantes/count")
    public ResponseEntity<Map<String, Integer>> contarEstudiantes(@PathVariable Integer id) {
        Integer cantidad = cursoService.contarEstudiantesPorCurso(id);
        return ResponseEntity.ok(Map.of("cantidad", cantidad));
    }

    @PostMapping("/{cursoId}/docente/{docenteId}")
    public ResponseEntity<?> asignarDocente(
            @PathVariable Integer cursoId,
            @PathVariable Integer docenteId) {
        Optional<Curso> cursoOpt = cursoService.findById(cursoId);
        Optional<Docente> docenteOpt = docenteService.findById(docenteId);

        if (cursoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (docenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Docente no encontrado"));
        }

        Curso curso = cursoOpt.get();
        Docente docente = docenteOpt.get();

        curso.setDocente(docente);
        curso = cursoService.save(curso);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Docente asignado correctamente");
        response.put("curso_id", cursoId);
        response.put("docente", Map.of(
            "id", docente.getId_docente(),
            "nombre", docente.getNombre_docente() + " " + docente.getApellido_docente()
        ));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cursoId}/docente")
    public ResponseEntity<?> desasignarDocente(@PathVariable Integer cursoId) {
        Optional<Curso> cursoOpt = cursoService.findById(cursoId);
        
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Curso curso = cursoOpt.get();
        if (curso.getDocente() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "El curso no tiene docente asignado"));
        }

        curso.setDocente(null);
        cursoService.save(curso);

        return ResponseEntity.ok(Map.of("mensaje", "Docente desasignado correctamente"));
    }

    @GetMapping("/{cursoId}/docente")
    public ResponseEntity<?> obtenerDocenteCurso(@PathVariable Integer cursoId) {
        Optional<Curso> cursoOpt = cursoService.findById(cursoId);
        
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Curso curso = cursoOpt.get();
        Docente docente = curso.getDocente();

        if (docente == null) {
            return ResponseEntity.ok(Map.of("mensaje", "El curso no tiene docente asignado"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id_docente", docente.getId_docente());
        response.put("nombre", docente.getNombre_docente());
        response.put("apellido", docente.getApellido_docente());

        return ResponseEntity.ok(response);
    }    
  }