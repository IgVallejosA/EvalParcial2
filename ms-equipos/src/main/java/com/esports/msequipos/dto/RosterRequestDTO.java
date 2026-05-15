package com.esports.msequipos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RosterRequestDTO {

    @NotNull(message = "El ID del jugador es obligatorio")
    @Positive(message = "El ID del jugador debe ser positivo")
    private Long idJugador;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
