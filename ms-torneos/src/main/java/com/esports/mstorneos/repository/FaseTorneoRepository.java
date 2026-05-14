package com.esports.mstorneos.repository;

import com.esports.mstorneos.model.FaseTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaseTorneoRepository extends JpaRepository<FaseTorneo, Long> {
    List<FaseTorneo> findByTorneoIdOrderByOrden(Long torneoId);
    boolean existsByTorneoIdAndOrden(Long torneoId, Integer orden);
}
