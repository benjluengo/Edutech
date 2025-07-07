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
        Faker faker = new Faker(new java.util.Locale("es", "CL"));
        Random random = new Random();

        // Generar usuarios
        for (int i = 0; i < 10; i++) {
            Usuario usuario = new Usuario();
            // Generar un RUN/RUT chileno válido con dígito verificador
            String run = ChileanRunGenerator.generate();
            usuario.setRun(run);
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

        // Lista de carreras realistas chilenas
        String[] carreras = {
            "Ingeniería Comercial", "Ingeniería Civil", "Ingeniería en Informática", "Derecho", "Medicina",
            "Psicología", "Enfermería", "Kinesiología", "Odontología", "Arquitectura",
            "Contador Auditor", "Periodismo", "Trabajo Social", "Educación Parvularia", "Fonoaudiología",
            "Nutrición y Dietética", "Técnico en Enfermería", "Técnico en Administración", "Técnico en Construcción", "Técnico en Electricidad"
        };

        // Generar cursos
        for (int i = 0; i < 10; i++) {
            Curso curso = new Curso();
            curso.setNombre_curso(carreras[random.nextInt(carreras.length)]);
            curso.setDescripcion(faker.lorem().sentence());
            curso.setPrecio(10000 + random.nextDouble() * 990000); // Precios en CLP
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
            evaluacion.setPuntaje_maximo(10 + random.nextInt(91)); // 10-100
            evaluacion.setCurso(cursoRepository.findAll().get(random.nextInt(10)));
            evaluacion.setDocente(docenteRepository.findAll().get(random.nextInt(5)));
            evaluacionRepository.save(evaluacion);
        }

        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Generar entregas
        for (int i = 0; i < 30; i++) {
            Entrega entrega = new Entrega();
            entrega.setContenido_entrega("Respuestas del examen " + (i + 1));
            entrega.setEstado_entrega("PENDIENTE");
            entrega.setEvaluacion(evaluaciones.get(random.nextInt(evaluaciones.size())));
            entrega.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
            entregaRepository.save(entrega);
        }
    }

    // Utilidad interna para generar RUN/RUT chileno válido
    private static class ChileanRunGenerator {
        private static final Random random = new Random();
        public static String generate() {
            int number = 1000000 + random.nextInt(90000000); // 7-8 digits
            return number + "-" + calculateCheckDigit(number);
        }
        private static String calculateCheckDigit(int rut) {
            int m = 0, s = 1;
            while (rut != 0) {
                s = (s + rut % 10 * (9 - m++ % 6)) % 11;
                rut /= 10;
            }
            if (s == 0) return "K";
            if (s == 1) return "0";
            return Integer.toString(11 - s);
        }
    }
}