package com.esports.msstreamers.repository;

import com.esports.msstreamers.model.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamerRepository extends JpaRepository<Streamer, Long> {
    boolean existsByNombreArtistico(String nombreArtistico);

    List<Streamer> findByIdioma(String idioma);

    List<Streamer> findByRol(String rol);

    List<Streamer> findByActivoTrue();
}
