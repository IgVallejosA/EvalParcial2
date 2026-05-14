package com.esports.mstorneos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TorneoRequestDTO {

    @NotBlank(message = "El nombre del torneo es obligatorio")
    @Size(min = 3, max = 150)
    private String nombre;

    @NotNull(message = "El ID del juego es obligatorio")
    @Positive
    private Long idJuego;

    @NotBlank(message = "El organizador es obligatorio")
    @Size(max = 100)
    private String organizador;

    @NotNull
    @FutureOrPresent(message = "La fecha de inicio no puede ser pasada")
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    @Positive(message = "El premio debe ser positivo")
    private Double premioTotalUsd;

    @NotNull
    @Min(value = 2, message = "Mínimo 2 equipos")
    @Max(value = 64, message = "Máximo 64 equipos")
    private Integer maxEquipos;

    @NotBlank
    @Pattern(regexp = "PRESENCIAL|ONLINE|HIBRIDO",
             message = "Modalidad: PRESENCIAL, ONLINE o HIBRIDO")
    private String modalidad;
}
