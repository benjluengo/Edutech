package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.controller.CursoController;
import com.edutech.cl.edutech.controller.UsuarioController;
import com.edutech.cl.edutech.controller.InscripcionController;
import com.edutech.cl.edutech.controller.EvaluacionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CursoModelAssembler implements RepresentationModelAssembler<Curso, EntityModel<Curso>> {
    @Override
    public @NonNull EntityModel<Curso> toModel(@NonNull Curso curso) {
        return EntityModel.of(curso,
            linkTo(methodOn(CursoController.class).getCursoById(curso.getId_curso())).withSelfRel(),
            linkTo(methodOn(CursoController.class).getAllCursos()).withRel("cursos"),
            linkTo(methodOn(InscripcionController.class).obtenerInscripciones()).withRel("inscripciones"),
            linkTo(methodOn(EvaluacionController.class).getAllEvaluaciones()).withRel("evaluaciones")
        );
    }

    @Override
    public @NonNull CollectionModel<EntityModel<Curso>> toCollectionModel(@NonNull Iterable<? extends Curso> entities) {
        CollectionModel<EntityModel<Curso>> cursos = RepresentationModelAssembler.super.toCollectionModel(entities);
        cursos.add(linkTo(methodOn(CursoController.class).getAllCursos()).withSelfRel());
        cursos.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        cursos.add(linkTo(methodOn(InscripcionController.class).obtenerInscripciones()).withRel("inscripciones"));
        return cursos;
    }
}
