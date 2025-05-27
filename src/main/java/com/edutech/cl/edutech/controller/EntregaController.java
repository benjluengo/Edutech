package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @GetMapping
    public ResponseEntity<List<Entrega>> getAllEntregas() {
        return ResponseEntity.ok(entregaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> getEntregaById(@PathVariable Integer id) {
        return entregaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/usuarios/{userId}/evaluaciones/{evaluacionId}")
    public ResponseEntity<?> crearEntrega(
            @PathVariable Integer userId,
            @PathVariable Integer evaluacionId,
            @RequestBody Map<String, String> body) {
        
        try {
            String contenido = body.get("contenido");
            Entrega nuevaEntrega = entregaService.crearEntrega(userId, evaluacionId, contenido);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Entrega creada exitosamente");
            response.put("entrega", nuevaEntrega);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoEntrega(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        
        try {
            String nuevoEstado = body.get("estado");
            Entrega entregaActualizada = entregaService.actualizarEstadoEntrega(id, nuevoEstado);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Estado de entrega actualizado exitosamente");
            response.put("entrega", entregaActualizada);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/usuarios/{userId}")
    public ResponseEntity<List<Entrega>> getEntregasByUsuario(@PathVariable Integer userId) {
        return ResponseEntity.ok(entregaService.findByUsuarioId(userId));
    }

    @GetMapping("/evaluaciones/{evaluacionId}")
    public ResponseEntity<List<Entrega>> getEntregasByEvaluacion(@PathVariable Integer evaluacionId) {
        return ResponseEntity.ok(entregaService.findByEvaluacionId(evaluacionId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Entrega>> getEntregasByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(entregaService.findByEstado(estado));
    }
}
