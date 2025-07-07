package com.edutech.cl.edutech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_docente;

    @Column(nullable = false)
    private String nombre_docente;

    @Column(nullable = false)
    private String apellido_docente;

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("docente")
    private List<Curso> cursos = new ArrayList<>();
}
