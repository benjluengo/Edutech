package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.controller.UsuarioController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {
    @Override
    public @NonNull EntityModel<Usuario> toModel(@NonNull Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).getProfile(usuario.getId_usuario())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).getProfile(usuario.getId_usuario())).withRel("usuarios")
        );
    }
}
