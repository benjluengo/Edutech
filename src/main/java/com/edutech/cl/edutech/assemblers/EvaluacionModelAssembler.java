package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.controller.EvaluacionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EvaluacionModelAssembler implements RepresentationModelAssembler<Evaluacion, EntityModel<Evaluacion>> {
    @Override
    public @NonNull EntityModel<Evaluacion> toModel(@NonNull Evaluacion evaluacion) {
        return EntityModel.of(evaluacion,
            linkTo(methodOn(EvaluacionController.class).getEvaluacionById(evaluacion.getId_evaluacion())).withSelfRel(),
            linkTo(methodOn(EvaluacionController.class).getAllEvaluaciones()).withRel("evaluaciones")
        );
    }
}
