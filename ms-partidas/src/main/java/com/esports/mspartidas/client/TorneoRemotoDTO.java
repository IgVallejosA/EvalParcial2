package com.esports.mspartidas.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TorneoRemotoDTO {
    private Long id;
    private String nombre;
    private String estado;
}
