package com.esports.msestadisticas.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaRemotoDTO {
    private Long id;
    private Long idTorneo;
    private String estado;
}
