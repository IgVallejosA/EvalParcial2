package com.esports.msestadisticas.repository;

import com.esports.msestadisticas.model.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
    List<Estadistica> findByIdJugador(Long idJugador);

    List<Estadistica> findByIdPartida(Long idPartida);

    Optional<Estadistica> findByIdJugadorAndIdPartida(Long idJugador, Long idPartida);

    boolean existsByIdJugadorAndIdPartida(Long idJugador, Long idPartida);

    List<Estadistica> findByMvpTrue();
}
