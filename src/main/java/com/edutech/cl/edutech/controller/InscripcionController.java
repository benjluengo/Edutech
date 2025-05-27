package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.service.InscripcionService;
import com.edutech.cl.edutech.service.UsuarioService;
import com.edutech.cl.edutech.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {    @Autowired
    private InscripcionService inscripcionService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CursoService cursoService;

    private Map<String, Object> convertToMap(Inscripcion inscripcion) {
        Map<String, Object> map = new HashMap<>();
        map.put("id_inscripcion", inscripcion.getId_inscripcion());
        map.put("id_usuario", inscripcion.getUsuario().getId_usuario());
        map.put("nombre_usuario", inscripcion.getUsuario().getNombre_usuario() + " " + inscripcion.getUsuario().getApellido_usuario());
        map.put("id_curso", inscripcion.getCurso().getId_curso());
        map.put("nombre_curso", inscripcion.getCurso().getNombre_curso());
        map.put("fecha_inscripcion", inscripcion.getFecha_inscripcion());
        return map;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerInscripciones() {
        List<Map<String, Object>> inscripciones = inscripcionService.findAll()
            .stream()
            .map(this::convertToMap)
            .collect(Collectors.toList());
        return ResponseEntity.ok(inscripciones);
    }    
    
    @PostMapping("/sales")
    public ResponseEntity<?> inscribirConPago(@RequestBody Inscripcion inscripcion) {
        boolean pagoExitoso = true;

        if (pagoExitoso) {
            Usuario usuario = usuarioService.findById(inscripcion.getUsuario().getId_usuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Curso curso = cursoService.findById(inscripcion.getCurso().getId_curso()).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            
            // Actualizar la inscripción con los datos completos
            inscripcion.setUsuario(usuario);
            inscripcion.setCurso(curso);
            
            Inscripcion nuevaInscripcion = inscripcionService.save(inscripcion);
            Map<String, Object> inscripcionMap = convertToMap(nuevaInscripcion);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "mensaje", "Inscripción y pago realizados con éxito",
                        "inscripcion", inscripcionMap
                    ));
        } else {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body(Map.of("mensaje", "El pago no fue exitoso"));
        }
    }
}