package com.esports.mspatrocinadores.repository;

import com.esports.mspatrocinadores.model.Patrocinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {
    boolean existsByNombreEmpresa(String nombreEmpresa);

    List<Patrocinador> findByTier(String tier);

    List<Patrocinador> findByIndustria(String industria);

    List<Patrocinador> findByActivoTrue();
}
