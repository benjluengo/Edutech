package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.service.InscripcionService;
import com.edutech.cl.edutech.service.UsuarioService;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.assemblers.InscripcionModelAssembler;
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
import java.util.stream.Collectors;

@Tag(name = "Gestión de Inscripciones", description = "API para gestionar inscripciones a cursos")
@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private InscripcionModelAssembler inscripcionModelAssembler;

    @Operation(summary = "Listar todas las inscripciones", description = "Obtiene una lista de todas las inscripciones con información detallada")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida correctamente", content = @Content(schema = @Schema(implementation = Inscripcion.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> obtenerInscripciones() {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.findAll().stream().map(inscripcionModelAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(inscripciones));
    }    
    
    @Operation(summary = "Realizar inscripción con pago", description = "Procesa una nueva inscripción incluyendo el pago del curso")
    @ApiResponse(responseCode = "201", description = "Inscripción y pago realizados correctamente")
    @ApiResponse(responseCode = "402", description = "Error en el pago")
    @ApiResponse(responseCode = "404", description = "Usuario o curso no encontrado")
    @PostMapping("/sales")
    public ResponseEntity<EntityModel<Inscripcion>> inscribirConPago(
        @Parameter(description = "Datos de la inscripción")
        @RequestBody Inscripcion inscripcion) {
            boolean pagoExitoso = true;
            if (pagoExitoso) {
                Usuario usuario = usuarioService.findById(inscripcion.getUsuario().getId_usuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Curso curso = cursoService.findById(inscripcion.getCurso().getId_curso()).orElseThrow(() -> new RuntimeException("Curso no encontrado"));

                inscripcion.setUsuario(usuario);
                inscripcion.setCurso(curso);
                Inscripcion nuevaInscripcion = inscripcionService.save(inscripcion);
                return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionModelAssembler.toModel(nuevaInscripcion));
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(inscripcionModelAssembler.toModel(new Inscripcion()));
            }
    }

    @Operation(summary = "Obtener inscripciones por curso", description = "Lista todas las inscripciones asociadas a un curso específico")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida correctamente")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> getInscripcionesByCursoId(@PathVariable Integer cursoId) {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.findByCursoId(cursoId).stream()
            .map(inscripcionModelAssembler::toModel)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(inscripciones));
    }

    @Operation(summary = "Obtener inscripciones por usuario", description = "Lista todas las inscripciones asociadas a un usuario específico")
    @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida correctamente")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> getInscripcionesByUsuarioId(@PathVariable Integer usuarioId) {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.findByUsuarioId(usuarioId).stream()
            .map(inscripcionModelAssembler::toModel)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(inscripciones));
    }
}