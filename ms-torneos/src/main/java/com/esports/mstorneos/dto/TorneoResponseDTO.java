package com.esports.mstorneos.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TorneoResponseDTO {
    private Long id;
    private String nombre;
    private Long idJuego;
    private String nombreJuego;
    private String organizador;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer duracionDias;
    private Double premioTotalUsd;
    private Integer maxEquipos;
    private String modalidad;
    private String estado;
    private List<FaseResponseDTO> fases;
}
