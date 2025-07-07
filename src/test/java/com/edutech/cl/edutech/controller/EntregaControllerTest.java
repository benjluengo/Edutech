package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Entrega;
import com.edutech.cl.edutech.service.EntregaService;
import com.edutech.cl.edutech.assemblers.EntregaModelAssembler;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(EntregaController.class)
public class EntregaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntregaService entregaService;
    @MockBean
    private EntregaModelAssembler entregaModelAssembler;

    @Test
    void shouldReturnOkForGetAllEntregas() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(1);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.findAll()).thenReturn(List.of(entrega));
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(get("/api/entregas")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.entregaList[0].id_entrega", is(1)));
    }

    @Test
    void shouldReturnEntregaById() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(2);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.findById(2)).thenReturn(Optional.of(entrega));
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(get("/api/entregas/2")).andExpect(status().isOk()).andExpect(jsonPath("id_entrega", is(2)));
    }

    @Test
    void shouldCrearEntrega() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(3);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.crearEntrega(eq(1), eq(2), eq("contenido"))).thenReturn(entrega);
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(post("/api/entregas/usuarios/1/evaluaciones/2").contentType(MediaType.APPLICATION_JSON).content("{\"contenido\":\"contenido\"}")).andExpect(status().isCreated()).andExpect(jsonPath("id_entrega", is(3)));
    }

    @Test
    void shouldActualizarEstadoEntrega() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(4);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.actualizarEstadoEntrega(eq(4), eq("APROBADA"))).thenReturn(entrega);
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(put("/api/entregas/4/estado").contentType(MediaType.APPLICATION_JSON).content("{\"estado\":\"APROBADA\"}")).andExpect(status().isOk()).andExpect(jsonPath("id_entrega", is(4)));
    }

    @Test
    void shouldGetEntregasByUsuario() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(5);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.findByUsuarioId(1)).thenReturn(List.of(entrega));
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(get("/api/entregas/usuarios/1")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.entregaList[0].id_entrega", is(5)));
    }

    @Test
    void shouldGetEntregasByEvaluacion() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(6);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.findByEvaluacionId(2)).thenReturn(List.of(entrega));
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(get("/api/entregas/evaluaciones/2")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.entregaList[0].id_entrega", is(6)));
    }

    @Test
    void shouldGetEntregasByEstado() throws Exception {
        Entrega entrega = new Entrega();
        entrega.setId_entrega(7);
        EntityModel<Entrega> model = EntityModel.of(entrega);
        Mockito.when(entregaService.findByEstado("REVISADA")).thenReturn(List.of(entrega));
        Mockito.when(entregaModelAssembler.toModel(any(Entrega.class))).thenReturn(model);
        mockMvc.perform(get("/api/entregas/estado/REVISADA")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.entregaList[0].id_entrega", is(7)));
    }
}
