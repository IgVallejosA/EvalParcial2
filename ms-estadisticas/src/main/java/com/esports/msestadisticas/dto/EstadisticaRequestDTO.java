package com.esports.msestadisticas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadisticaRequestDTO {

    @NotNull
    @Positive
    private Long idJugador;

    @NotNull
    @Positive
    private Long idPartida;

    @NotNull
    @PositiveOrZero
    private Integer kills;

    @NotNull
    @PositiveOrZero
    private Integer deaths;

    @NotNull
    @PositiveOrZero
    private Integer assists;

    @PositiveOrZero
    private Integer damageDealt;

    @Positive
    private Integer tiempoJugadoMinutos;

    @NotNull
    private Boolean mvp;
}
