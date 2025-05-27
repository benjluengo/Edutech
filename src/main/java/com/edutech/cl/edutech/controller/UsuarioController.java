package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        // Validar si el RUN ya existe
        if (usuarioService.buscarPorRun(usuario.getRun()) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "El RUN ya está registrado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Usuario nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String run = credentials.get("run");
        String contraseña = credentials.get("contraseña");

        Usuario usuario = usuarioService.buscarPorRun(run);
        
        if (usuario != null && contraseña.equals(usuario.getContraseña())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                           .body(Map.of("mensaje", "Credenciales inválidas"));
    }    
    
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Integer id) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id_usuario", usuario.getId_usuario());
            response.put("run", usuario.getRun());
            response.put("nombre_usuario", usuario.getNombre_usuario());
            response.put("apellido_usuario", usuario.getApellido_usuario());
            
            // Simplificar la información de las inscripciones
            List<Map<String, Object>> inscripcionesSimplificadas = usuario.getInscripciones().stream()
                .map(inscripcion -> {
                    Map<String, Object> inscripcionMap = new HashMap<>();
                    inscripcionMap.put("id_inscripcion", inscripcion.getId_inscripcion());
                    inscripcionMap.put("fecha_inscripcion", inscripcion.getFecha_inscripcion());
                    
                    Map<String, Object> cursoMap = new HashMap<>();
                    cursoMap.put("id_curso", inscripcion.getCurso().getId_curso());
                    cursoMap.put("nombre_curso", inscripcion.getCurso().getNombre_curso());
                    cursoMap.put("descripcion", inscripcion.getCurso().getDescripcion());
                    inscripcionMap.put("curso", cursoMap);
                    
                    return inscripcionMap;
                })
                .collect(Collectors.toList());
            
            response.put("inscripciones", inscripcionesSimplificadas);
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestBody Map<String, String> passwords) {
        String newPassword = passwords.get("newPassword");
        String currentPassword = passwords.get("currentPassword");
        
        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            
            // Verificar que la contraseña actual sea correcta
            if (!usuario.getContraseña().equals(currentPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "La contraseña actual es incorrecta"));
            }
            
            // Actualizar la contraseña
            usuario.setContraseña(newPassword);
            usuarioService.save(usuario);
            
            return ResponseEntity.ok().body(Map.of("mensaje", "Contraseña actualizada correctamente"));
        }
        
        return ResponseEntity.notFound().build();
    }
}
