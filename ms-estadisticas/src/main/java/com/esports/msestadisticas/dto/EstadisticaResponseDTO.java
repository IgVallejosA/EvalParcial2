package com.esports.msestadisticas.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadisticaResponseDTO {
    private Long id;
    private Long idJugador;
    private String nicknameJugador;
    private Long idPartida;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Double kda;
    private Integer damageDealt;
    private Integer tiempoJugadoMinutos;
    private Boolean mvp;
    private LocalDateTime fechaRegistro;
}
