package com.esports.mstransferencias.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransferenciaResponseDTO {
    private Long id;
    private Long idJugador;
    private String nicknameJugador;
    private Long idEquipoOrigen;
    private String nombreEquipoOrigen;
    private Long idEquipoDestino;
    private String nombreEquipoDestino;
    private LocalDate fechaTransferencia;
    private Double montoUsd;
    private String tipo;
    private Integer duracionContratoMeses;
    private String observaciones;
}
