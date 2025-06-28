package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.service.DocenteService;
import com.edutech.cl.edutech.service.EvaluacionService;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.assemblers.EvaluacionModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Gestión de Evaluaciones", description = "API para administrar evaluaciones de cursos")
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

    @Autowired
    private EvaluacionModelAssembler evaluacionModelAssembler;

    @Operation(summary = "Obtener todas las evaluaciones", description = "Retorna una lista de todas las evaluaciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida correctamente", content = @Content(schema = @Schema(implementation = Evaluacion.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Evaluacion>>> getAllEvaluaciones() {
        List<EntityModel<Evaluacion>> evaluaciones = evaluacionService.findAll().stream().map(evaluacionModelAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(evaluaciones));
    }

    @Operation(summary = "Obtener evaluación por ID", description = "Busca y retorna una evaluación específica por su ID")
    @ApiResponse(responseCode = "200", description = "Evaluación encontrada")
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Evaluacion>> getEvaluacionById(
        @Parameter(description = "ID de la evaluación a buscar")
        @PathVariable Integer id) {
            return evaluacionService.findById(id).map(evaluacionModelAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear evaluación para curso y docente", description = "Crea una nueva evaluación asociada a un curso y un docente específicos")
    @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente")
    @ApiResponse(responseCode = "404", description = "Curso o docente no encontrado")
    @ApiResponse(responseCode = "400", description = "El docente no está asignado al curso")
    @PostMapping("/curso/{cursoId}/docente/{docenteId}")
    public ResponseEntity<EntityModel<Evaluacion>> crearEvaluacion(
        @Parameter(description = "ID del curso")
        @PathVariable Integer cursoId,
        @Parameter(description = "ID del docente")
        @PathVariable Integer docenteId,
        @Parameter(description = "Datos de la evaluación")
        @RequestBody Evaluacion evaluacion) {
            Optional<Curso> cursoOpt = cursoService.findById(cursoId);
            Optional<Docente> docenteOpt = docenteService.findById(docenteId);

            if (cursoOpt.isEmpty() || docenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(evaluacionModelAssembler.toModel(new Evaluacion()));
            }

            Docente docente = docenteOpt.get();
            Curso curso = cursoOpt.get();
            if (curso.getDocente() == null || !curso.getDocente().getId_docente().equals(docenteId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(evaluacionModelAssembler.toModel(new Evaluacion()));
            }

            evaluacion.setCurso(curso);
            evaluacion.setDocente(docente);
            Evaluacion nuevaEvaluacion = evaluacionService.save(evaluacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionModelAssembler.toModel(nuevaEvaluacion));
    }

    @Operation(summary = "Actualizar evaluación", description = "Actualiza la información de una evaluación existente")
    @ApiResponse(responseCode = "200", description = "Evaluación actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Evaluacion>> updateEvaluacion(
        @Parameter(description = "ID de la evaluación")
        @PathVariable Integer id,
        @Parameter(description = "Nuevos datos de la evaluación")
        @RequestBody Evaluacion evaluacion) {
            Optional<Evaluacion> evaluacionExistente = evaluacionService.findById(id);
            
            if (evaluacionExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Evaluacion eval = evaluacionExistente.get();
            eval.setPuntaje_maximo(evaluacion.getPuntaje_maximo());
            Evaluacion updated = evaluacionService.save(eval);
            return ResponseEntity.ok(evaluacionModelAssembler.toModel(updated));
    }

    @Operation(summary = "Eliminar evaluación", description = "Elimina una evaluación del sistema")
    @ApiResponse(responseCode = "200", description = "Evaluación eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluacion(
        @Parameter(description = "ID de la evaluación")
        @PathVariable Integer id) {
            if (evaluacionService.findById(id).isPresent()) {
                evaluacionService.deleteById(id);
                return ResponseEntity.ok(Map.of("mensaje", "Evaluación eliminada correctamente"));
            }
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener docente de evaluación", description = "Obtiene la información del docente asignado a una evaluación")
    @ApiResponse(responseCode = "200", description = "Información del docente obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    @GetMapping("/{evaluacionId}/docente")
    public ResponseEntity<Docente> getDocenteByEvaluacion(
        @Parameter(description = "ID de la evaluación")
        @PathVariable Integer evaluacionId) {
            Optional<Evaluacion> evaluacion = evaluacionService.findById(evaluacionId);
            if (evaluacion.isPresent() && evaluacion.get().getDocente() != null) {
                return ResponseEntity.ok(evaluacion.get().getDocente());
            }
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener evaluaciones por curso", description = "Lista todas las evaluaciones asociadas a un curso específico")
    @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/cursos/{cursoId}")
    public ResponseEntity<CollectionModel<EntityModel<Evaluacion>>> getEvaluacionesByCurso(
        @Parameter(description = "ID del curso")
        @PathVariable Integer cursoId) {
            Optional<Curso> curso = cursoService.findById(cursoId);
            if (curso.isPresent()) {
                List<EntityModel<Evaluacion>> evaluaciones = curso.get().getEvaluaciones().stream().map(evaluacionModelAssembler::toModel).collect(Collectors.toList());
                return ResponseEntity.ok(CollectionModel.of(evaluaciones));
            }
            return ResponseEntity.notFound().build();
    }
}
