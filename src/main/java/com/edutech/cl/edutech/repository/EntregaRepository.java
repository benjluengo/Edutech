package com.edutech.cl.edutech.repository;

import com.edutech.cl.edutech.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    
    @Query("SELECT e FROM Entrega e WHERE e.usuario.id_usuario = :userId")
    List<Entrega> findByUsuarioId(@Param("userId") Integer userId);
    
    @Query("SELECT e FROM Entrega e WHERE e.evaluacion.id_evaluacion = :evaluacionId")
    List<Entrega> findByEvaluacionId(@Param("evaluacionId") Integer evaluacionId);
    
    @Query("SELECT e FROM Entrega e WHERE e.estado_entrega = :estado")
    List<Entrega> findByEstado(@Param("estado") String estado);
}
