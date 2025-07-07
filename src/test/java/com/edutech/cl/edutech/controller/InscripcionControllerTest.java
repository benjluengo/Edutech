package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.assemblers.InscripcionModelAssembler;
import com.edutech.cl.edutech.model.Curso;
import com.edutech.cl.edutech.model.Inscripcion;
import com.edutech.cl.edutech.model.Usuario;
import com.edutech.cl.edutech.service.CursoService;
import com.edutech.cl.edutech.service.InscripcionService;
import com.edutech.cl.edutech.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscripcionController.class)
public class InscripcionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InscripcionService inscripcionService;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private CursoService cursoService;
    @MockBean
    private InscripcionModelAssembler inscripcionModelAssembler;

    @Test
    void shouldReturnOkForGetAllInscripciones() throws Exception {
        mockMvc.perform(get("/api/inscripciones")).andExpect(status().isOk());
    }

    @Test
    void shouldObtenerInscripciones() throws Exception {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(1);
        EntityModel<Inscripcion> model = EntityModel.of(inscripcion);
        Mockito.when(inscripcionService.findAll()).thenReturn(List.of(inscripcion));
        Mockito.when(inscripcionModelAssembler.toModel(any(Inscripcion.class))).thenReturn(model);
        mockMvc.perform(get("/api/inscripciones")).andExpect(status().isOk()).andExpect(jsonPath("_embedded.inscripcionList[0].id_inscripcion", is(1)));
    }

    @Test
    void shouldInscribirConPago() throws Exception {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(2);
        Usuario usuario = new Usuario();
        usuario.setId_usuario(1);
        Curso curso = new Curso();
        curso.setId_curso(1);
        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);
        EntityModel<Inscripcion> model = EntityModel.of(inscripcion);
        Mockito.when(usuarioService.findById(1)).thenReturn(Optional.of(usuario));
        Mockito.when(cursoService.findById(1)).thenReturn(Optional.of(curso));
        Mockito.when(inscripcionService.save(any(Inscripcion.class))).thenReturn(inscripcion);
        Mockito.when(inscripcionModelAssembler.toModel(any(Inscripcion.class))).thenReturn(model);
        mockMvc.perform(post("/api/inscripciones/sales").contentType(MediaType.APPLICATION_JSON).content("{\"usuario\":{\"id_usuario\":1},\"curso\":{\"id_curso\":1}}")).andExpect(status().isCreated()).andExpect(jsonPath("id_inscripcion", is(2)));
    }

    @Test
    void shouldGetInscripcionesByCursoId() throws Exception {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(100);
        EntityModel<Inscripcion> model = EntityModel.of(inscripcion);
        Mockito.when(inscripcionService.findByCursoId(1)).thenReturn(List.of(inscripcion));
        Mockito.when(inscripcionModelAssembler.toModel(any(Inscripcion.class))).thenReturn(model);
        mockMvc.perform(get("/api/inscripciones/curso/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.inscripcionList[0].id_inscripcion", is(100)));
    }

    @Test
    void shouldGetInscripcionesByUsuarioId() throws Exception {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(200);
        EntityModel<Inscripcion> model = EntityModel.of(inscripcion);
        Mockito.when(inscripcionService.findByUsuarioId(2)).thenReturn(List.of(inscripcion));
        Mockito.when(inscripcionModelAssembler.toModel(any(Inscripcion.class))).thenReturn(model);
        mockMvc.perform(get("/api/inscripciones/usuario/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.inscripcionList[0].id_inscripcion", is(200)));
    }
}
