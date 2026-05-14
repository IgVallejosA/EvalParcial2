package com.esports.mspartidas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaRequestDTO {

    @NotNull
    @Positive
    private Long idTorneo;

    @NotNull
    @Positive
    private Long idEquipoLocal;

    @NotNull
    @Positive
    private Long idEquipoVisitante;

    @NotNull
    @Future(message = "La fecha de la partida debe ser futura")
    private LocalDateTime fechaHora;

    @Size(max = 80)
    private String mapaOEscenario;
}
