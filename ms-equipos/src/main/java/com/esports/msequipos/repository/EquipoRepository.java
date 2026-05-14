package com.esports.msequipos.repository;

import com.esports.msequipos.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    Optional<Equipo> findByNombreAndRegion(String nombre, String region);

    boolean existsByNombreAndRegion(String nombre, String region);

    List<Equipo> findByRegion(String region);

    List<Equipo> findByActivoTrue();
}
