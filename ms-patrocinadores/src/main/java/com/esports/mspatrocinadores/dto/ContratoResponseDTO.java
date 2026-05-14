package com.esports.mspatrocinadores.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContratoResponseDTO {
    private Long id;
    private Long idEquipo;
    private String nombreEquipo;
    private Double montoAnualUsd;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoAcuerdo;
    private Boolean activo;
}
