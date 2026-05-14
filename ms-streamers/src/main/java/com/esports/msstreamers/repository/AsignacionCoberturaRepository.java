package com.esports.msstreamers.repository;

import com.esports.msstreamers.model.AsignacionCobertura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionCoberturaRepository extends JpaRepository<AsignacionCobertura, Long> {
    List<AsignacionCobertura> findByStreamerId(Long streamerId);
    List<AsignacionCobertura> findByIdTorneo(Long idTorneo);
    boolean existsByStreamerIdAndIdTorneo(Long streamerId, Long idTorneo);
}
