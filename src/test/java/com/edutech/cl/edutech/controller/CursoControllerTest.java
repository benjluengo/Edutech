package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Docente;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.service.DocenteService;
import com.edutech.cl.edutech.assemblers.CursoModelAssembler;
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

@WebMvcTest(CursoController.class)
public class CursoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;
    @MockBean
    private DocenteService docenteService;
    @MockBean
    private CursoModelAssembler cursoModelAssembler;

    @Test
    void shouldReturnOkForGetAllCursos() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(1);
        curso.setNombre_curso("Matemáticas");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.findAll()).thenReturn(List.of(curso));
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(get("/api/inventory/cursos")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.cursoList[0].id_curso", is(1))).andExpect(jsonPath("_embedded.cursoList[0].nombre_curso", is("Matemáticas")));
    }

    @Test
    void shouldReturnCursoById() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(2);
        curso.setNombre_curso("Historia");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.findById(2)).thenReturn(Optional.of(curso));
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(get("/api/inventory/cursos/2")).andExpect(status().isOk()).andExpect(jsonPath("id_curso", is(2))).andExpect(jsonPath("nombre_curso", is("Historia")));
    }

    @Test
    void shouldCreateCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(3);
        curso.setNombre_curso("Ciencias");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.save(any(Curso.class))).thenReturn(curso);
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(post("/api/inventory/cursos").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Ciencias\"}")).andExpect(status().isCreated()).andExpect(jsonPath("id_curso", is(3))).andExpect(jsonPath("nombre_curso", is("Ciencias")));
    }

    @Test
    void shouldUpdateCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(4);
        curso.setNombre_curso("Arte");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.findById(4)).thenReturn(Optional.of(curso));
        Mockito.when(cursoService.save(any(Curso.class))).thenReturn(curso);
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(put("/api/inventory/cursos/4").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Arte\"}")).andExpect(status().isOk()).andExpect(jsonPath("id_curso", is(4))).andExpect(jsonPath("nombre_curso", is("Arte")));
    }

    @Test
    void shouldDeleteCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(5);
        Mockito.when(cursoService.findById(5)).thenReturn(Optional.of(curso));
        mockMvc.perform(delete("/api/inventory/cursos/5")).andExpect(status().isOk()).andExpect(jsonPath("mensaje", is("Curso eliminado correctamente")));
    }

    @Test
    void shouldBuscarPorNombre() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(6);
        curso.setNombre_curso("Física");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.buscarPorNombre("Física")).thenReturn(List.of(curso));
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(get("/api/inventory/cursos/buscar?nombre=Física")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.cursoList[0].nombre_curso", is("Física")));
    }

    @Test
    void shouldBuscarPorPrecio() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(7);
        curso.setNombre_curso("Química");
        EntityModel<Curso> model = EntityModel.of(curso);
        Mockito.when(cursoService.buscarPorRangoPrecio(100.0, 200.0)).thenReturn(List.of(curso));
        Mockito.when(cursoModelAssembler.toModel(any(Curso.class))).thenReturn(model);
        mockMvc.perform(get("/api/inventory/cursos/precio?precioMin=100&precioMax=200")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.cursoList[0].nombre_curso", is("Química")));
    }

    @Test
    void shouldContarEstudiantes() throws Exception {
        Mockito.when(cursoService.contarEstudiantesPorCurso(8)).thenReturn(25);
        mockMvc.perform(get("/api/inventory/cursos/8/estudiantes/count")).andExpect(status().isOk()).andExpect(jsonPath("cantidad", is(25)));
    }

    @Test
    void shouldAsignarDocente() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(9);
        Docente docente = new Docente();
        docente.setId_docente(1);
        docente.setNombre_docente("Juan");
        docente.setApellido_docente("Pérez");
        Mockito.when(cursoService.findById(9)).thenReturn(Optional.of(curso));
        Mockito.when(docenteService.findById(1)).thenReturn(Optional.of(docente));
        Mockito.when(cursoService.save(any(Curso.class))).thenReturn(curso);
        mockMvc.perform(post("/api/inventory/cursos/9/docente/1")).andExpect(status().isOk()).andExpect(jsonPath("mensaje", is("Docente asignado correctamente"))).andExpect(jsonPath("curso_id", is(9))).andExpect(jsonPath("docente.id", is(1))).andExpect(jsonPath("docente.nombre", is("Juan Pérez")));
    }

    @Test
    void shouldDesasignarDocente() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(10);
        Docente docente = new Docente();
        docente.setId_docente(2);
        curso.setDocente(docente);
        Mockito.when(cursoService.findById(10)).thenReturn(Optional.of(curso));
        Mockito.when(cursoService.save(any(Curso.class))).thenReturn(curso);
        mockMvc.perform(delete("/api/inventory/cursos/10/docente")).andExpect(status().isOk()).andExpect(jsonPath("mensaje", is("Docente desasignado correctamente")));
    }

    @Test
    void shouldObtenerDocenteCurso() throws Exception {
        Curso curso = new Curso();
        curso.setId_curso(11);
        Docente docente = new Docente();
        docente.setId_docente(3);
        docente.setNombre_docente("Ana");
        docente.setApellido_docente("García");
        curso.setDocente(docente);
        Mockito.when(cursoService.findById(11)).thenReturn(Optional.of(curso));
        mockMvc.perform(get("/api/inventory/cursos/11/docente")).andExpect(status().isOk()).andExpect(jsonPath("id_docente", is(3))).andExpect(jsonPath("nombre", is("Ana"))).andExpect(jsonPath("apellido", is("García")));
    }
}
