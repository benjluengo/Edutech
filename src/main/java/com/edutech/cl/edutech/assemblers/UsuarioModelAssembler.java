package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.controller.UsuarioController;
import com.edutech.cl.edutech.controller.InscripcionController;
import com.edutech.cl.edutech.controller.EntregaController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
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
            linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"),
            linkTo(methodOn(InscripcionController.class).obtenerInscripciones()).withRel("inscripciones"),
            linkTo(methodOn(EntregaController.class).getEntregasByUsuario(usuario.getId_usuario())).withRel("entregas")
        );
    }

    @Override
    public @NonNull CollectionModel<EntityModel<Usuario>> toCollectionModel(@NonNull Iterable<? extends Usuario> entities) {
        CollectionModel<EntityModel<Usuario>> usuarios = RepresentationModelAssembler.super.toCollectionModel(entities);
        usuarios.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withSelfRel());
        usuarios.add(linkTo(methodOn(InscripcionController.class).obtenerInscripciones()).withRel("inscripciones"));
        usuarios.add(linkTo(methodOn(EntregaController.class).getAllEntregas()).withRel("entregas"));
        return usuarios;
    }
}
