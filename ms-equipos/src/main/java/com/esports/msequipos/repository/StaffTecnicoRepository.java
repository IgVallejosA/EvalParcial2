package com.esports.msequipos.repository;

import com.esports.msequipos.model.StaffTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffTecnicoRepository extends JpaRepository<StaffTecnico, Long> {

    List<StaffTecnico> findByEquipoId(Long equipoId);

    List<StaffTecnico> findByEquipoIdAndRolStaffAndActivoTrue(Long equipoId, String rolStaff);
}
