package com.esports.msestadisticas.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ResumenJugadorDTO {
    private Long idJugador;
    private String nicknameJugador;
    private Integer partidasJugadas;
    private Integer totalKills;
    private Integer totalDeaths;
    private Integer totalAssists;
    private Double kdaPromedio;
    private Integer mvps;
}
