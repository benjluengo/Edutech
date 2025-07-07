package com.edutech.cl.edutech.repository;

import com.edutech.cl.edutech.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    // Buscar inscripciones por ID de usuario (SQL nativo)
    @Query(value = "SELECT * FROM inscripcion WHERE id_usuario = :userId", nativeQuery = true)
    List<Inscripcion> buscarPorUserId(@Param("userId") Integer userId);

    // Buscar inscripciones por ID de curso (JPQL)
    @Query("SELECT i FROM Inscripcion i WHERE i.curso.id_curso = :cursoId")
    List<Inscripcion> buscarPorCursoId(@Param("cursoId") Integer cursoId);

    // Contar inscripciones por curso (SQL nativo)
    @Query(value = "SELECT COUNT(*) FROM inscripcion WHERE id_curso = :cursoId", nativeQuery = true)
    Integer contarPorCursoId(@Param("cursoId") Integer cursoId);

    // Buscar inscripciones por rango de fechas (SQL nativo)
    @Query(value = "SELECT * FROM inscripcion WHERE fecha_inscripcion BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    List<Inscripcion> buscarPorFecha(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Verificar si un usuario está inscrito en un curso (JPQL)
    @Query("SELECT COUNT(i) > 0 FROM Inscripcion i WHERE i.usuario.id_usuario = :userId AND i.curso.id_curso = :cursoId")
    boolean existePorUserIdYCursoId(@Param("userId") Integer userId, @Param("cursoId") Integer cursoId);
}
