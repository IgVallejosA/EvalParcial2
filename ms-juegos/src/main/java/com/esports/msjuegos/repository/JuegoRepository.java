package com.esports.msjuegos.repository;

import com.esports.msjuegos.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    Optional<Juego> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Juego> findByGenero(String genero);

    List<Juego> findByDesarrolladora(String desarrolladora);

    List<Juego> findByActivoTrue();
}
