package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.assemblers.EntregaModelAssembler;
import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.service.EntregaService;
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
import java.util.stream.Collectors;

@Tag(name = "Gestión de Entregas", description = "API para gestionar las entregas de evaluaciones por parte de los estudiantes")
@RestController
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @Autowired
    private EntregaModelAssembler entregaModelAssembler;

    @Operation(summary = "Obtener todas las entregas", description = "Retorna una lista de todas las entregas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida correctamente", content = @Content(schema = @Schema(implementation = Entrega.class)))
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Entrega>>> getAllEntregas() {
        List<EntityModel<Entrega>> entregas = entregaService.findAll().stream().map(entregaModelAssembler::toModel).collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(entregas));
    }

    @Operation(summary = "Obtener entrega por ID", description = "Busca y retorna una entrega específica por su ID")
    @ApiResponse(responseCode = "200", description = "Entrega encontrada")
    @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Entrega>> getEntregaById(
        @Parameter(description = "ID de la entrega a buscar")
        @PathVariable Integer id) {
            return entregaService.findById(id).map(entregaModelAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nueva entrega", description = "Crea una nueva entrega para una evaluación por parte de un estudiante")
    @ApiResponse(responseCode = "201", description = "Entrega creada exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario o evaluación no encontrada")
    @PostMapping("/usuarios/{userId}/evaluaciones/{evaluacionId}")
    public ResponseEntity<EntityModel<Entrega>> crearEntrega(
        @Parameter(description = "ID del usuario que realiza la entrega")
        @PathVariable Integer userId,
        @Parameter(description = "ID de la evaluación")
        @PathVariable Integer evaluacionId,
        @Parameter(description = "Contenido de la entrega")
        @RequestBody Map<String, String> body) {
            try {
                String contenido = body.get("contenido");
                Entrega nuevaEntrega = entregaService.crearEntrega(userId, evaluacionId, contenido);
                return ResponseEntity.status(HttpStatus.CREATED).body(entregaModelAssembler.toModel(nuevaEntrega));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(entregaModelAssembler.toModel(new Entrega()));
            }
    }

    @Operation(summary = "Actualizar estado de entrega", description = "Actualiza el estado de una entrega existente (ej: PENDIENTE, REVISADA, APROBADA)")
    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    @PutMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Entrega>> actualizarEstadoEntrega(
        @Parameter(description = "ID de la entrega")
        @PathVariable Integer id,
        @Parameter(description = "Nuevo estado de la entrega")
        @RequestBody Map<String, String> body) {
            try {
                String nuevoEstado = body.get("estado");
                Entrega entregaActualizada = entregaService.actualizarEstadoEntrega(id, nuevoEstado);
                return ResponseEntity.ok(entregaModelAssembler.toModel(entregaActualizada));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(entregaModelAssembler.toModel(new Entrega()));
            }
    }

    @Operation(summary = "Obtener entregas por usuario", description = "Lista todas las entregas realizadas por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida correctamente")
    @GetMapping("/usuarios/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<Entrega>>> getEntregasByUsuario(
        @Parameter(description = "ID del usuario")
        @PathVariable Integer userId) {
            List<EntityModel<Entrega>> entregas = entregaService.findByUsuarioId(userId).stream().map(entregaModelAssembler::toModel).collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(entregas));
    }

    @Operation(summary = "Obtener entregas por evaluación", description = "Lista todas las entregas asociadas a una evaluación específica")
    @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida correctamente")
    @GetMapping("/evaluaciones/{evaluacionId}")
    public ResponseEntity<CollectionModel<EntityModel<Entrega>>> getEntregasByEvaluacion(
        @Parameter(description = "ID de la evaluación")
        @PathVariable Integer evaluacionId) {
            List<EntityModel<Entrega>> entregas = entregaService.findByEvaluacionId(evaluacionId).stream().map(entregaModelAssembler::toModel).collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(entregas));
    }

    @Operation(summary = "Obtener entregas por estado", description = "Lista todas las entregas que se encuentran en un estado específico")
    @ApiResponse(responseCode = "200", description = "Lista de entregas obtenida correctamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Entrega>>> getEntregasByEstado(
        @Parameter(description = "Estado de las entregas a buscar")
        @PathVariable String estado) {
            List<EntityModel<Entrega>> entregas = entregaService.findByEstado(estado).stream().map(entregaModelAssembler::toModel).collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(entregas));
    }
}
