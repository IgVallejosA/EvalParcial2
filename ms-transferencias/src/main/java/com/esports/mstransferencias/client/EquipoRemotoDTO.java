package com.esports.mstransferencias.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EquipoRemotoDTO {
    private Long id;
    private String nombre;
    private Boolean activo;
}
