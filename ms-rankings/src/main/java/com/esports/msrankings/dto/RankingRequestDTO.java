package com.esports.msrankings.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RankingRequestDTO {

    @NotBlank
    @Pattern(regexp = "EQUIPO|JUGADOR", message = "tipoEntidad: EQUIPO o JUGADOR")
    private String tipoEntidad;

    @NotNull
    @Positive
    private Long idEntidad;

    @NotBlank
    @Size(max = 20)
    private String region;

    @NotNull
    @PositiveOrZero
    private Double puntos;

    @PositiveOrZero
    private Integer victorias;

    @PositiveOrZero
    private Integer derrotas;
}
