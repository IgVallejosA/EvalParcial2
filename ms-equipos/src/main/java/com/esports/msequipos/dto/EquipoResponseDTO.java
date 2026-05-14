package com.esports.msequipos.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoResponseDTO {
    private Long id;
    private String nombre;
    private String region;
    private LocalDate fechaFundacion;
    private Integer rankingMundial;
    private Double presupuestoAnualUsd;
    private Boolean activo;
    private Integer cantidadStaff;
    private Integer cantidadJugadoresActuales;
    private List<StaffResponseDTO> staff;
    private List<RosterResponseDTO> roster;
}
