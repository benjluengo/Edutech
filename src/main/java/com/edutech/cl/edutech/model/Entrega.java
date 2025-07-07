package com.edutech.cl.edutech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_entrega;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"inscripciones", "entregas", "contraseña"})
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_evaluacion", nullable = false)
    @JsonIgnoreProperties({"entregas", "curso", "docente"})
    private Evaluacion evaluacion;

    @Column(nullable = false)
    private String contenido_entrega;

    @Column(nullable = false)
    private String estado_entrega;
}
