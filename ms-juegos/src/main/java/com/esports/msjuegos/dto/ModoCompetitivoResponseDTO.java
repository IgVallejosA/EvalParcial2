package com.esports.msjuegos.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ModoCompetitivoResponseDTO {
    private Long id;
    private String nombreModo;
    private Integer maxJugadoresPorEquipo;
    private Integer duracionPromedioMinutos;
    private Boolean activo;
}
