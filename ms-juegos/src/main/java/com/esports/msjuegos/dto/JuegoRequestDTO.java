package com.esports.msjuegos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoRequestDTO {

    @NotBlank(message = "El nombre del juego es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "MOBA|FPS|RTS|FIGHTING|BR|CARDS|SPORTS", message = "Género inválido. Valores permitidos: MOBA, FPS, RTS, FIGHTING, BR, CARDS, SPORTS")
    private String genero;

    @NotBlank(message = "La desarrolladora es obligatoria")
    @Size(max = 100)
    private String desarrolladora;

    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    @PastOrPresent(message = "La fecha de lanzamiento no puede ser futura")
    private LocalDate fechaLanzamiento;

    @NotBlank(message = "La plataforma es obligatoria")
    @Pattern(regexp = "^(PC|PS5|PS4|XBOX|SWITCH|MOBILE)(,(PC|PS5|PS4|XBOX|SWITCH|MOBILE))*$", message = "Plataformas válidas separadas por coma: PC, PS5, PS4, XBOX, SWITCH, MOBILE")
    private String plataforma;

    @PositiveOrZero(message = "El prize pool no puede ser negativo")
    private Double prizePoolTotalUsd;
}
