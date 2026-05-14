package com.esports.mstransferencias.repository;

import com.esports.mstransferencias.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByIdJugadorOrderByFechaTransferenciaDesc(Long idJugador);
    List<Transferencia> findByIdEquipoOrigenOrIdEquipoDestino(Long idOrigen, Long idDestino);
    List<Transferencia> findByTipo(String tipo);
}
