package com.esports.mspatrocinadores.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContratoRequestDTO {

    @NotNull
    @Positive
    private Long idEquipo;

    @NotNull
    @Positive
    private Double montoAnualUsd;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotBlank
    @Pattern(regexp = "JERSEY|NAMING_RIGHTS|EVENT|MERCHANDISE",
             message = "Tipo: JERSEY, NAMING_RIGHTS, EVENT o MERCHANDISE")
    private String tipoAcuerdo;
}
