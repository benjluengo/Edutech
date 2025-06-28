package com.edutech.cl.edutech.assemblers;

import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.controller.EntregaController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EntregaModelAssembler implements RepresentationModelAssembler<Entrega, EntityModel<Entrega>> {
    @Override
    public @NonNull EntityModel<Entrega> toModel(@NonNull Entrega entrega) {
        return EntityModel.of(entrega,
            linkTo(methodOn(EntregaController.class).getEntregaById(entrega.getId_entrega())).withSelfRel(),
            linkTo(methodOn(EntregaController.class).getAllEntregas()).withRel("entregas")
        );
    }
}
