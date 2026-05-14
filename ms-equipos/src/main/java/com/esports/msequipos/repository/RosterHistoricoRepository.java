package com.esports.msequipos.repository;

import com.esports.msequipos.model.RosterHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RosterHistoricoRepository extends JpaRepository<RosterHistorico, Long> {

    List<RosterHistorico> findByEquipoId(Long equipoId);

    List<RosterHistorico> findByEquipoIdAndFechaFinIsNull(Long equipoId);

    List<RosterHistorico> findByIdJugador(Long idJugador);
}
