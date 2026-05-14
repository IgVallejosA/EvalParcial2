package com.esports.mstransferencias.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransferenciaRequestDTO {

    @NotNull
    @Positive
    private Long idJugador;

    @Positive
    private Long idEquipoOrigen;

    @Positive
    private Long idEquipoDestino;

    @NotNull
    @PastOrPresent(message = "La fecha de transferencia no puede ser futura")
    private LocalDate fechaTransferencia;

    @PositiveOrZero
    private Double montoUsd;

    @NotBlank
    @Pattern(regexp = "TRANSFERENCIA|FICHAJE_INICIAL|BAJA|PRESTAMO",
             message = "Tipo: TRANSFERENCIA, FICHAJE_INICIAL, BAJA o PRESTAMO")
    private String tipo;

    @Positive
    private Integer duracionContratoMeses;

    @Size(max = 500)
    private String observaciones;
}
