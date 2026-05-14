package com.esports.mspartidas.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaResponseDTO {
    private Long id;
    private Long idTorneo;
    private String nombreTorneo;
    private Long idEquipoLocal;
    private String nombreEquipoLocal;
    private Long idEquipoVisitante;
    private String nombreEquipoVisitante;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private Long idEquipoGanador;
    private String estado;
    private String mapaOEscenario;
}
