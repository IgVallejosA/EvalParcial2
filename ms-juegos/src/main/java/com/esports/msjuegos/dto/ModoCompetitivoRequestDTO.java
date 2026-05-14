package com.esports.msjuegos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ModoCompetitivoRequestDTO {

    @NotBlank(message = "El nombre del modo es obligatorio")
    @Size(min = 2, max = 80)
    private String nombreModo;

    @NotNull(message = "Debe especificar el máximo de jugadores por equipo")
    @Min(value = 1, message = "Mínimo 1 jugador por equipo")
    @Max(value = 10, message = "Máximo 10 jugadores por equipo")
    private Integer maxJugadoresPorEquipo;

    @Positive(message = "La duración debe ser positiva")
    private Integer duracionPromedioMinutos;
}
