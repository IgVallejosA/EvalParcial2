package com.esports.msequipos.client;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorRemotoDTO {
    private Long id;
    private String nickname;
    private String nombreReal;
    private String pais;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String rol;
    private Long idEquipoActual;
    private Boolean activo;
}
