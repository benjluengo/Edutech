package com.edutech.cl.edutech;

import com.edutech.cl.edutech.model.*;
import com.edutech.cl.edutech.repository.*;
import net.datafaker.Faker;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        Random random = new Random();

        // Generar usuarios
        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            usuario.setRun(faker.idNumber().valid());
            usuario.setNombre_usuario(faker.name().firstName());
            usuario.setApellido_usuario(faker.name().lastName());
            usuario.setContraseña(faker.internet().password());
            usuarioRepository.save(usuario);
        }

        // Generar docentes
        for (int i = 0; i < 5; i++) {
            Docente docente = new Docente();
            docente.setNombre_docente(faker.name().firstName());
            docente.setApellido_docente(faker.name().lastName());
            docenteRepository.save(docente);
        }

        // Generar cursos
        for (int i = 0; i < 10; i++) {
            Curso curso = new Curso();
            curso.setNombre_curso(faker.educator().course());
            curso.setDescripcion(faker.lorem().sentence());
            curso.setPrecio(random.nextDouble() * 1000);
            curso.setDocente(docenteRepository.findAll().get(random.nextInt(5)));
            cursoRepository.save(curso);
        }

        // Generar inscripciones
        for (int i = 0; i < 20; i++) {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setFecha_inscripcion(new Date());
            inscripcion.setUsuario(usuarioRepository.findAll().get(random.nextInt(10)));
            inscripcion.setCurso(cursoRepository.findAll().get(random.nextInt(10)));
            inscripcionRepository.save(inscripcion);
        }

        // Generar evaluaciones
        for (int i = 0; i < 15; i++) {
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setPuntaje_maximo(random.nextInt(100));
            evaluacion.setCurso(cursoRepository.findAll().get(random.nextInt(10)));
            evaluacion.setDocente(docenteRepository.findAll().get(random.nextInt(5)));
            evaluacionRepository.save(evaluacion);
        }

        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Generar entregas
        for (int i = 0; i < 30; i++) {
            Entrega entrega = new Entrega();
            entrega.setContenido_entrega("Respuestas del examen" + (i + 1));
            entrega.setEstado_entrega("PENDIENTE");
            entrega.setEvaluacion(evaluaciones.get(random.nextInt(evaluaciones.size())));
            entrega.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
            entregaRepository.save(entrega);
        }
    }
}