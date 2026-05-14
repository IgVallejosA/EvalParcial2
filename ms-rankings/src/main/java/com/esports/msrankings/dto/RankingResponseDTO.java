package com.esports.msrankings.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RankingResponseDTO {
    private Long id;
    private String tipoEntidad;
    private Long idEntidad;
    private String nombreEntidad;
    private String region;
    private Double puntos;
    private Integer posicion;
    private Integer victorias;
    private Integer derrotas;
    private Double winRate;
    private LocalDateTime fechaActualizacion;
}
