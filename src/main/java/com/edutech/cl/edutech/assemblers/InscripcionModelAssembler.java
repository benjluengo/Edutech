package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.controller.InscripcionController;
import com.edutech.cl.edutech.controller.CursoController;
import com.edutech.cl.edutech.controller.UsuarioController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InscripcionModelAssembler implements RepresentationModelAssembler<Inscripcion, EntityModel<Inscripcion>> {
    @Override
    public @NonNull EntityModel<Inscripcion> toModel(@NonNull Inscripcion inscripcion) {
        return EntityModel.of(inscripcion,
            linkTo(methodOn(InscripcionController.class).obtenerInscripciones()).withRel("inscripciones"),
            linkTo(methodOn(CursoController.class).getCursoById(inscripcion.getCurso().getId_curso())).withRel("curso"),
            linkTo(methodOn(UsuarioController.class).getProfile(inscripcion.getUsuario().getId_usuario())).withRel("usuario")
        );
    }
}
