package com.esports.mstorneos.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JuegoRemotoDTO {
    private Long id;
    private String nombre;
    private String genero;
    private String desarrolladora;
    private Boolean activo;
}
