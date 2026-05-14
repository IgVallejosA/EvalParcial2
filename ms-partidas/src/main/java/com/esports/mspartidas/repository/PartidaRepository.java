package com.esports.mspartidas.repository;

import com.esports.mspartidas.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByIdTorneo(Long idTorneo);

    List<Partida> findByEstado(String estado);

    List<Partida> findByIdEquipoLocalOrIdEquipoVisitante(Long idLocal, Long idVisitante);
}
