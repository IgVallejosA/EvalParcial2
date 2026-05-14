package com.esports.mstorneos.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FaseResponseDTO {
    private Long id;
    private String nombreFase;
    private Integer orden;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String formato;
}
