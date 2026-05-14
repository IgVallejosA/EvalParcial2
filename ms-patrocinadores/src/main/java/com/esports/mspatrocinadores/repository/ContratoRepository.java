package com.esports.mspatrocinadores.repository;

import com.esports.mspatrocinadores.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByPatrocinadorId(Long idPatrocinador);

    List<Contrato> findByIdEquipo(Long idEquipo);

    List<Contrato> findByActivoTrue();
}
