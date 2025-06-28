package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Evaluacion;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.service.EvaluacionService;
import com.edutech.cl.edutech.service.DocenteService;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.assemblers.EvaluacionModelAssembler;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(EvaluacionController.class)
public class EvaluacionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EvaluacionService evaluacionService;
    @MockBean
    private DocenteService docenteService;
    @MockBean
    private CursoService cursoService;
    @MockBean
    private EvaluacionModelAssembler evaluacionModelAssembler;

    @Test
    void shouldReturnOkForGetAllEvaluaciones() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(1);
        EntityModel<Evaluacion> model = EntityModel.of(evaluacion);
        Mockito.when(evaluacionService.findAll()).thenReturn(List.of(evaluacion));
        Mockito.when(evaluacionModelAssembler.toModel(any(Evaluacion.class))).thenReturn(model);
        mockMvc.perform(get("/api/evaluaciones")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.evaluacionList[0].id_evaluacion", is(1)));
    }

    @Test
    void shouldReturnEvaluacionById() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(2);
        EntityModel<Evaluacion> model = EntityModel.of(evaluacion);
        Mockito.when(evaluacionService.findById(2)).thenReturn(Optional.of(evaluacion));
        Mockito.when(evaluacionModelAssembler.toModel(any(Evaluacion.class))).thenReturn(model);
        mockMvc.perform(get("/api/evaluaciones/2")).andExpect(status().isOk()).andExpect(jsonPath("id_evaluacion", is(2)));
    }

    @Test
    void shouldCrearEvaluacion() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(3);
        EntityModel<Evaluacion> model = EntityModel.of(evaluacion);
        Curso curso = new Curso();
        curso.setId_curso(1);
        Docente docente = new Docente();
        docente.setId_docente(2);
        curso.setDocente(docente);
        Mockito.when(cursoService.findById(1)).thenReturn(Optional.of(curso));
        Mockito.when(docenteService.findById(2)).thenReturn(Optional.of(docente));
        Mockito.when(evaluacionService.save(any(Evaluacion.class))).thenReturn(evaluacion);
        Mockito.when(evaluacionModelAssembler.toModel(any(Evaluacion.class))).thenReturn(model);
        mockMvc.perform(post("/api/evaluaciones/curso/1/docente/2").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isCreated()).andExpect(jsonPath("id_evaluacion", is(3)));
    }

    @Test
    void shouldUpdateEvaluacion() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(4);
        EntityModel<Evaluacion> model = EntityModel.of(evaluacion);
        Mockito.when(evaluacionService.findById(4)).thenReturn(Optional.of(evaluacion));
        Mockito.when(evaluacionService.save(any(Evaluacion.class))).thenReturn(evaluacion);
        Mockito.when(evaluacionModelAssembler.toModel(any(Evaluacion.class))).thenReturn(model);
        mockMvc.perform(put("/api/evaluaciones/4").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isOk()).andExpect(jsonPath("id_evaluacion", is(4)));
    }

    @Test
    void shouldDeleteEvaluacion() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(5);
        Mockito.when(evaluacionService.findById(5)).thenReturn(Optional.of(evaluacion));
        mockMvc.perform(delete("/api/evaluaciones/5")).andExpect(status().isOk()).andExpect(jsonPath("mensaje", is("Evaluación eliminada correctamente")));
    }

    @Test
    void shouldGetDocenteByEvaluacion() throws Exception {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(6);
        Docente docente = new Docente();
        docente.setId_docente(3);
        evaluacion.setDocente(docente);
        Mockito.when(evaluacionService.findById(6)).thenReturn(Optional.of(evaluacion));
        mockMvc.perform(get("/api/evaluaciones/6/docente")).andExpect(status().isOk()).andExpect(jsonPath("id_docente", is(3)));
    }

    @Test
    void shouldGetEvaluacionesByCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(7);
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId_evaluacion(8);
        curso.setEvaluaciones(List.of(evaluacion));
        EntityModel<Evaluacion> model = EntityModel.of(evaluacion);
        Mockito.when(cursoService.findById(7)).thenReturn(Optional.of(curso));
        Mockito.when(evaluacionModelAssembler.toModel(any(Evaluacion.class))).thenReturn(model);
        mockMvc.perform(get("/api/evaluaciones/cursos/7")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.evaluacionList[0].id_evaluacion", is(8)));
    }
}
