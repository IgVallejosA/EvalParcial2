package com.esports.msrankings.repository;

import com.esports.msrankings.model.RankingEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingEntradaRepository extends JpaRepository<RankingEntrada, Long> {

        List<RankingEntrada> findByTipoEntidadAndRegionOrderByPuntosDesc(String tipoEntidad, String region);

        Optional<RankingEntrada> findByTipoEntidadAndIdEntidadAndRegion(
                        String tipoEntidad, Long idEntidad, String region);

        boolean existsByTipoEntidadAndIdEntidadAndRegion(
                        String tipoEntidad, Long idEntidad, String region);

        List<RankingEntrada> findByTipoEntidad(String tipoEntidad);
}
