package com.esports.mspatrocinadores.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EquipoRemotoDTO {
    private Long id;
    private String nombre;
    private String region;
    private Boolean activo;
}
