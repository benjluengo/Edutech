package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.controller.CursoController;
import org.springframework.hateoas.EntityModel;
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
            linkTo(methodOn(CursoController.class).getAllCursos()).withRel("cursos")
        );
    }
}
