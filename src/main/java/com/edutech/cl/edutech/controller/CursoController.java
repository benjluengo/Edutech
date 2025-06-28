package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.assemblers.CursoModelAssembler;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.service.DocenteService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Gestión de Cursos", description = "API para administrar cursos y sus asignaciones")
@RestController
@RequestMapping("/api/inventory/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private CursoModelAssembler cursoModelAssembler;

    @Operation(summary = "Obtener todos los cursos", description = "Retorna una lista de todos los cursos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida correctamente", content = @Content(schema = @Schema(implementation = Curso.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> getAllCursos() {
        List<EntityModel<Curso>> cursos = cursoService.findAll().stream().map(cursoModelAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(cursos));
    }

    @Operation(summary = "Obtener curso por ID", description = "Busca y retorna un curso específico por su ID")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> getCursoById(
        @Parameter(description = "ID del curso a buscar")
        @PathVariable Integer id) {
            return cursoService.findById(id).map(cursoModelAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Crear nuevo curso", description = "Crea un nuevo curso sin asignar docente")
    @ApiResponse(responseCode = "201", description = "Curso creado exitosamente")
    @PostMapping
    public ResponseEntity<EntityModel<Curso>> createCurso(
        @Parameter(description = "Datos del nuevo curso")
        @RequestBody Curso curso) {
            Curso saved = cursoService.save(curso);
            return new ResponseEntity<>(cursoModelAssembler.toModel(saved), HttpStatus.CREATED);
    }

    @Operation(summary = "Crear curso con docente asignado", description = "Crea un nuevo curso y lo asigna a un docente específico")
    @ApiResponse(responseCode = "201", description = "Curso creado y asignado exitosamente")
    @ApiResponse(responseCode = "400", description = "Docente no encontrado")
    @PostMapping("/docente/{docenteId}")
    public ResponseEntity<EntityModel<Curso>> createCursoConDocente(
        @Parameter(description = "ID del docente a asignar")
        @PathVariable Integer docenteId,
        @Parameter(description = "Datos del nuevo curso")
        @RequestBody Curso curso) {
            Optional<Docente> docente = docenteService.findById(docenteId);
            if (docente.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            
            curso.setDocente(docente.get());
            Curso cursoCreado = cursoService.save(curso);
            return new ResponseEntity<>(cursoModelAssembler.toModel(cursoCreado), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar curso", description = "Actualiza la información de un curso existente")
    @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> updateCurso(
        @Parameter(description = "ID del curso a actualizar")
        @PathVariable Integer id,
        @Parameter(description = "Nuevos datos del curso")
        @RequestBody Curso curso) {
            Optional<Curso> cursoExistente = cursoService.findById(id);
            
            if (cursoExistente.isPresent()) {
                curso.setId_curso(id);
                Curso updated = cursoService.save(curso);
                return ResponseEntity.ok(cursoModelAssembler.toModel(updated));
            }
            
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar curso", description = "Elimina un curso del sistema")
    @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(
        @Parameter(description = "ID del curso a eliminar")
        @PathVariable Integer id) {
            if (cursoService.findById(id).isPresent()) {
                cursoService.deleteById(id);
                return ResponseEntity.ok(Map.of("mensaje", "Curso eliminado correctamente"));
            }
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar cursos por nombre", description = "Busca cursos que contengan el texto especificado en su nombre")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente")
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> buscarPorNombre(
        @Parameter(description = "Texto a buscar en el nombre del curso")
        @RequestParam String nombre) {
            List<EntityModel<Curso>> cursos = cursoService.buscarPorNombre(nombre).stream().map(cursoModelAssembler::toModel).collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(cursos));
    }

    @Operation(summary = "Buscar cursos por rango de precio", description = "Filtra cursos por un rango de precios específico")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente")
    @GetMapping("/precio")
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> buscarPorPrecio(
        @Parameter(description = "Precio mínimo")
        @RequestParam Double precioMin,
        @Parameter(description = "Precio máximo")
        @RequestParam Double precioMax) {
            List<EntityModel<Curso>> cursos = cursoService.buscarPorRangoPrecio(precioMin, precioMax).stream().map(cursoModelAssembler::toModel).collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(cursos));
    }

    @Operation(summary = "Contar estudiantes en curso", description = "Obtiene el número total de estudiantes inscritos en un curso")
    @ApiResponse(responseCode = "200", description = "Conteo realizado correctamente")
    @GetMapping("/{id}/estudiantes/count")
    public ResponseEntity<Map<String, Integer>> contarEstudiantes(
        @Parameter(description = "ID del curso")
        @PathVariable Integer id) {
            Integer cantidad = cursoService.contarEstudiantesPorCurso(id);
            return ResponseEntity.ok(Map.of("cantidad", cantidad));
    }

    @Operation(summary = "Asignar docente a curso", description = "Asigna un docente específico a un curso")
    @ApiResponse(responseCode = "200", description = "Docente asignado correctamente")
    @ApiResponse(responseCode = "404", description = "Curso o docente no encontrado")
    @PostMapping("/{cursoId}/docente/{docenteId}")
    public ResponseEntity<?> asignarDocente(
        @Parameter(description = "ID del curso")
        @PathVariable Integer cursoId,
        @Parameter(description = "ID del docente")
        @PathVariable Integer docenteId) {
            Optional<Curso> cursoOpt = cursoService.findById(cursoId);
            Optional<Docente> docenteOpt = docenteService.findById(docenteId);

            if (cursoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (docenteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Docente no encontrado"));
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

    @Operation(summary = "Desasignar docente de curso", description = "Elimina la asignación del docente de un curso")
    @ApiResponse(responseCode = "200", description = "Docente desasignado correctamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @DeleteMapping("/{cursoId}/docente")
    public ResponseEntity<?> desasignarDocente(
        @Parameter(description = "ID del curso")
        @PathVariable Integer cursoId) {
            Optional<Curso> cursoOpt = cursoService.findById(cursoId);
            
            if (cursoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Curso curso = cursoOpt.get();
            if (curso.getDocente() == null) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "El curso no tiene docente asignado"));
            }

            curso.setDocente(null);
            cursoService.save(curso);

            return ResponseEntity.ok(Map.of("mensaje", "Docente desasignado correctamente"));
    }

    @Operation(summary = "Obtener docente de curso", description = "Obtiene la información del docente asignado a un curso")
    @ApiResponse(responseCode = "200", description = "Información del docente obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/{cursoId}/docente")
    public ResponseEntity<?> obtenerDocenteCurso(
        @Parameter(description = "ID del curso")
        @PathVariable Integer cursoId) {
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