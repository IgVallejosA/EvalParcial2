package com.esports.msstreamers.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AsignacionRequestDTO {

    @NotNull
    @Positive
    private Long idTorneo;

    @NotNull
    private LocalDate fechaInicioCobertura;

    @NotNull
    private LocalDate fechaFinCobertura;

    @PositiveOrZero
    private Double honorariosUsd;
}
