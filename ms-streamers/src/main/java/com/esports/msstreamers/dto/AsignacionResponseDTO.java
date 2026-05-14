package com.esports.msstreamers.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AsignacionResponseDTO {
    private Long id;
    private Long idTorneo;
    private String nombreTorneo;
    private LocalDate fechaInicioCobertura;
    private LocalDate fechaFinCobertura;
    private Double honorariosUsd;
}
