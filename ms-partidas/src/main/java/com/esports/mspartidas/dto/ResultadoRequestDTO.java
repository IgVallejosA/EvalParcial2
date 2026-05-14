package com.esports.mspartidas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ResultadoRequestDTO {

    @NotNull
    @PositiveOrZero
    private Integer marcadorLocal;

    @NotNull
    @PositiveOrZero
    private Integer marcadorVisitante;

    @Positive
    private Integer duracionMinutos;
}
