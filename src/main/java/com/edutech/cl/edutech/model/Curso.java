package com.edutech.cl.edutech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_curso;

    @Column(nullable = false)
    private String nombre_curso;

    @Column(nullable = false)
    private String descripcion;    
    
    @Column(nullable = false)
    private double precio;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("curso")
    private List<Inscripcion> inscripciones;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("curso")
    private List<Evaluacion> evaluaciones;

    @ManyToOne
    @JoinColumn(name = "id_docente", nullable = true)
    @JsonIgnoreProperties("cursos")
    private Docente docente;
}
