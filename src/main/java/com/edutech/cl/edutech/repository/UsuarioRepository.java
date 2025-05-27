package com.edutech.cl.edutech.repository;

import com.edutech.cl.edutech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Buscar por RUN (usando JPQL)
    @Query("SELECT u FROM Usuario u WHERE u.run = :run")
    Usuario buscarPorRun(@Param("run") String run);
    
    // Buscar por apellido (usando SQL nativo)
    @Query(value = "SELECT * FROM usuario WHERE apellido_usuario LIKE CONCAT('%', :apellido, '%')", nativeQuery = true)
    List<Usuario> buscarPorApellido(@Param("apellido") String apellido);
    
    // Buscar usuarios por curso inscrito
    @Query("SELECT u FROM Usuario u JOIN u.inscripciones i WHERE i.curso.id_curso = :cursoId")
    List<Usuario> buscarPorInscripcionCursoId(@Param("cursoId") Integer cursoId);
    
    // Verificar si existe un RUN
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.run = :run")
    boolean existeRun(@Param("run") String run);
}