package com.esports.msequipos.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StaffResponseDTO {
    private Long id;
    private String nombre;
    private String rolStaff;
    private Double salarioMensualUsd;
    private Boolean activo;
}
