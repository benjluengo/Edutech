package com.edutech.cl.edutech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_evaluacion;

    @Column(nullable = false)
    private Integer puntaje_maximo;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    @JsonIgnoreProperties({"evaluaciones", "inscripciones", "docente"})
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = false)
    @JsonIgnoreProperties({"cursos", "evaluaciones"})
    private Docente docente;

    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("evaluacion")
    private List<Entrega> entregas = new ArrayList<>();
}
