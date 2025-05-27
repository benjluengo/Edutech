package com.edutech.cl.edutech.repository;

import com.edutech.cl.edutech.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    @Query(value = "SELECT * FROM curso WHERE nombre_curso LIKE CONCAT('%', :nombre, '%')", nativeQuery = true)
    List<Curso> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Curso c WHERE c.docente.id_docente = :docenteId")
    List<Curso> buscarPorDocente(@Param("docenteId") Integer docenteId);

    @Query(value = "SELECT COUNT(i.id_inscripcion) FROM inscripcion i WHERE i.id_curso = :cursoId", nativeQuery = true)
    Integer contarEstudiantesPorCurso(@Param("cursoId") Integer cursoId);

    @Query(value = "SELECT * FROM curso c WHERE c.cupo_maximo > (SELECT COUNT(*) FROM inscripcion i WHERE i.id_curso = c.id_curso)", nativeQuery = true)
    List<Curso> buscarCursosDisponibles();

    @Query("SELECT c FROM Curso c WHERE c.precio BETWEEN :precioMin AND :precioMax")
    List<Curso> buscarPorRangoPrecio(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
}
