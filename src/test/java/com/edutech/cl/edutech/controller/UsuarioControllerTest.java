package com.edutech.cl.edutech.controller;

import com.edutech.cl.edutech.assemblers.UsuarioModelAssembler;
import com.edutech.cl.edutech.model.Usuario;
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

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private UsuarioModelAssembler usuarioModelAssembler;

    @Test
    void shouldRegisterUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(1);
        EntityModel<Usuario> model = EntityModel.of(usuario);
        Mockito.when(usuarioService.buscarPorRun("12345678-9")).thenReturn(null);
        Mockito.when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        Mockito.when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(model);
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"run\":\"12345678-9\"}")).andExpect(status().isCreated()).andExpect(jsonPath("id_usuario", is(1)));
    }

    @Test
    void shouldLoginUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(2);
        usuario.setRun("12345678-9");
        usuario.setContraseña("pass");
        EntityModel<Usuario> model = EntityModel.of(usuario);
        Mockito.when(usuarioService.buscarPorRun("12345678-9")).thenReturn(usuario);
        Mockito.when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(model);
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"run\":\"12345678-9\",\"contraseña\":\"pass\"}")).andExpect(status().isOk()).andExpect(jsonPath("id_usuario", is(2)));
    }

    @Test
    void shouldGetProfile() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(3);
        EntityModel<Usuario> model = EntityModel.of(usuario);
        Mockito.when(usuarioService.findById(3)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(model);
        mockMvc.perform(get("/api/auth/profile/3")).andExpect(status().isOk()).andExpect(jsonPath("id_usuario", is(3)));
    }

    @Test
    void shouldUpdatePassword() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(4);
        usuario.setContraseña("oldpass");
        EntityModel<Usuario> model = EntityModel.of(usuario);
        Mockito.when(usuarioService.findById(4)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioService.save(any(Usuario.class))).thenReturn(usuario);
        Mockito.when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(model);
        mockMvc.perform(put("/api/auth/profile/4").contentType(MediaType.APPLICATION_JSON).content("{\"currentPassword\":\"oldpass\",\"newPassword\":\"newpass\"}")).andExpect(status().isOk()).andExpect(jsonPath("id_usuario", is(4)));
    }

    @Test
    void shouldGetAllUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId_usuario(10);
        EntityModel<Usuario> model = EntityModel.of(usuario);
        Mockito.when(usuarioService.findAll()).thenReturn(List.of(usuario));
        Mockito.when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(model);
        mockMvc.perform(get("/api/auth/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.usuarioList[0].id_usuario", is(10)));
    }
}
