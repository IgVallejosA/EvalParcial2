package com.esports.msequipos.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterResponseDTO {
    private Long id;
    private Long idJugador;
    private String nicknameAlUnirse;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
}
