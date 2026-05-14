package com.esports.msjuegos.repository;

import com.esports.msjuegos.model.ModoCompetitivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModoCompetitivoRepository extends JpaRepository<ModoCompetitivo, Long> {

    List<ModoCompetitivo> findByJuegoId(Long juegoId);

    boolean existsByJuegoIdAndNombreModo(Long juegoId, String nombreModo);
}
