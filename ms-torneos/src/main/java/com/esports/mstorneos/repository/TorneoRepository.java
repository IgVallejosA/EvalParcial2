package com.esports.mstorneos.repository;

import com.esports.mstorneos.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByEstado(String estado);

    List<Torneo> findByIdJuego(Long idJuego);

    boolean existsByNombre(String nombre);
}
