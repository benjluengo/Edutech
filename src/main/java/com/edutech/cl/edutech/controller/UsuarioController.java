package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.assemblers.UsuarioModelAssembler;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Autenticación y Gestión de Usuarios", description = "API para gestionar usuarios y autenticación")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en el sistema validando que el RUN no exista")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "400", description = "RUN ya registrado")
    @PostMapping("/register")
    public ResponseEntity<EntityModel<Usuario>> register(@RequestBody Usuario usuario) {
        // Validar si el RUN ya existe
        if (usuarioService.buscarPorRun(usuario.getRun()) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "El RUN ya está registrado");
            return ResponseEntity.badRequest().body(EntityModel.of(new Usuario()));
        }

        Usuario nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(usuarioModelAssembler.toModel(nuevoUsuario), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario usando su RUN y contraseña")
    @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String run = credentials.get("run");
        String contraseña = credentials.get("contraseña");
        Usuario usuario = usuarioService.buscarPorRun(run);
        
        if (usuario != null && contraseña.equals(usuario.getContraseña())) {
            return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Credenciales inválidas"));
    }    
    
    @Operation(summary = "Obtener perfil de usuario", description = "Obtiene información detallada del perfil de un usuario incluyendo sus inscripciones")
    @ApiResponse(responseCode = "200", description = "Perfil encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/profile/{id}")
    public ResponseEntity<EntityModel<Usuario>> getProfile(
        @Parameter(description = "ID del usuario a consultar")
        @PathVariable Integer id) {
            Optional<Usuario> usuarioOpt = usuarioService.findById(id);
            return usuarioOpt.map(usuarioModelAssembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar contraseña", description = "Actualiza la contraseña de un usuario verificando la contraseña actual")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updatePassword(
        @Parameter(description = "ID del usuario a actualizar")
        @PathVariable Integer id,
        @RequestBody Map<String, String> passwords) {
            String newPassword = passwords.get("newPassword");
            String currentPassword = passwords.get("currentPassword");
            Optional<Usuario> usuarioExistente = usuarioService.findById(id);

            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();
                if (!usuario.getContraseña().equals(currentPassword)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "La contraseña actual es incorrecta"));
                }
                
                usuario.setContraseña(newPassword);
                usuarioService.save(usuario);
                return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
            }

            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @GetMapping("/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
            .map(usuarioModelAssembler::toModel)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(usuarios));
    }
}
